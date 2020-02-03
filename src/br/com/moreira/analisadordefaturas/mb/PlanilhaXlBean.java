package br.com.moreira.analisadordefaturas.mb;

import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;

import br.com.moreira.analisadordefaturas.filtro.FiltroPlanilhaXL;
import br.com.moreira.analisadordefaturas.modelo.Fatura;

@ManagedBean
@ViewScoped
public class PlanilhaXlBean {
	FiltroPlanilhaXL filtroPlanilhaXL = new FiltroPlanilhaXL();
	
	private List<Fatura> faturasFiltradas;
	
	public PlanilhaXlBean() {
		
	}
	
	public FiltroPlanilhaXL getFiltroPlanilhaXL() {
		return filtroPlanilhaXL;
	}
	
	public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        
        wb.setSheetName(wb.getSheetIndex(sheet), "Planilha");
        
        HSSFRow header = sheet.getRow(0);
         
        HSSFCellStyle cellStyle = wb.createCellStyle();
        
        short border = HSSFCellStyle.BORDER_MEDIUM;
        cellStyle.setBorderTop(border);
        cellStyle.setBorderRight(border);
        cellStyle.setBorderBottom(border);
        cellStyle.setBorderLeft(border);
        
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);
         
        for(int i=0 ; i < header.getPhysicalNumberOfCells() ; i++) {
        	HSSFCell cell = header.getCell(i);
            cell.setCellStyle(cellStyle);
            
            sheet.autoSizeColumn(i);
        }
        
        converterCelulas(sheet);
        inserirFuncaoSomaTotal(header, sheet);
    }
	
	private void converterCelulas(HSSFSheet sheet) {
		HSSFRow row;
        double valor;
        for(int i = 1 ; i <= sheet.getLastRowNum() ; i++) {
        	row = sheet.getRow(i);
        	for(int j = 0 ; j < row.getLastCellNum() ; j++) {
        		if(row.getCell(j).getStringCellValue().contains("R$")) {
        			valor = Double.parseDouble(row.getCell(j).getStringCellValue().substring(3).replace(",", ".")); 
        			row.getCell(j).setCellValue(valor);
        		}
        	}
        }
	}
	
	private void inserirFuncaoSomaTotal(HSSFRow header, HSSFSheet sheet) {
		Iterator<Cell> iterator = header.cellIterator();
		Cell cell;
		int colIndex = 0;
		String colLetterIndex = "";
		while(iterator.hasNext()) {
			cell = iterator.next();
			if(cell.getStringCellValue().equals("Total Faturas")) {
				colIndex = cell.getColumnIndex();
				colLetterIndex = CellReference.convertNumToColString(cell.getColumnIndex());
			}
		}
		int firstRowIndex = 1;
		int lastRowIndex = sheet.getLastRowNum();
		sheet.createRow(lastRowIndex + 1);
		Row r = sheet.getRow(lastRowIndex + 1);
		r.createCell(colIndex);
		cell = r.getCell(colIndex);
		
		String formula = "SUM(" + colLetterIndex + firstRowIndex + ":" + colLetterIndex + (lastRowIndex + 1) + ")";
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(formula);
	}

	public List<Fatura> getFaturasFiltradas() {
		return faturasFiltradas;
	}

	public void setFaturasFiltradas(List<Fatura> faturasFiltradas) {
		this.faturasFiltradas = faturasFiltradas;
	}
	
}
