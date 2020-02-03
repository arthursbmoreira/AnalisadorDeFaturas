package br.com.moreira.analisadordefaturas.servico;


public class ConstrutorPlanilhaXL {

	@SuppressWarnings("unused")
	private final String dir = System.getProperty("user.home") + "/Desktop/";

//	public void gerarPlanilhaNumerica(List<Fatura> faturas) throws IOException,
//			WriteException {
//
//		Collections.sort(faturas, new Comparator<Fatura>() {
//			@Override
//			public int compare(Fatura f1, Fatura f2) {
//
//				return f1.getNumero().compareTo(f2.getNumero());
//			}
//		});
//
//		WritableWorkbook workbook = Workbook.createWorkbook(new File("PlanilhaXL.xls"));
//
//		WritableSheet sheet = workbook.createSheet("Planilha Numerica", 0);
//
//		configurarCabecalhoPlanilhaNumerica(sheet);
//
//		int linha = 1;
//		for (Fatura f : faturas) {

//			if (!f.getResponsavel().getNome().equals("")) {
//				Label nome = new Label(1, linha, f.getResponsavel().getNome());
//				sheet.addCell(nome);
//
//				Label numero = new Label(0, linha, f.getNumero());
//				sheet.addCell(numero);
//
//				if (f.getTotal().subtract(Fatura.VALOR_PLANO).compareTo(BigDecimal.ZERO) == 1) {
//					Label excedente = new Label(2, linha, "R$ " + (f.getTotal().subtract(Fatura.VALOR_PLANO)).toString());
//					sheet.addCell(excedente);
//				} else {
//					Label excedente = new Label(2, linha, (BigDecimal.ZERO).toString());
//					sheet.addCell(excedente);
//				}
//
//				if (f.getTempo() > Fatura.TEMPO_PLANO) {
//					jxl.write.Number minExcedente = new jxl.write.Number(3,
//							linha, f.getTempo() - Fatura.TEMPO_PLANO);
//					sheet.addCell(minExcedente);
//				} else {
//					jxl.write.Number minExcedente = new jxl.write.Number(3,
//							linha, 0);
//					sheet.addCell(minExcedente);
//				}
//
//				if (!f.getPlanoInternet().equals("")) {
//					String[] plano = f.getPlanoInternet().split("\\s+");
//					String mbPlano = plano[2];
//					Label planoInternet = new Label(4, linha, mbPlano);
//					sheet.addCell(planoInternet);
//				} else {
//					Label planoInternet = new Label(4, linha, "");
//					sheet.addCell(planoInternet);
//				}
//
//				if (!f.getPacoteMensagens().equals("")) {
//					Label pacoteSMS = new Label(5, linha,
//							f.getPacoteMensagens());
//					sheet.addCell(pacoteSMS);
//				} else {
//					Label pacoteSMS = new Label(5, linha, "");
//					sheet.addCell(pacoteSMS);
//				}
//
//				if (f.isIntragrupoDisponivel()) {
//					Label intragrupo = new Label(6, linha, "SIM");
//					sheet.addCell(intragrupo);
//				} else {
//					Label intragrupo = new Label(6, linha, "NãO");
//					sheet.addCell(intragrupo);
//				}
//
//				Label total = new Label(7, linha, "R$ "
//						+ f.getTotal().toString());
//				sheet.addCell(total);
//
//				linha++;
//			}
//		}
//		workbook.write();
//		workbook.close();
//	}
//
//	private void configurarCabecalhoPlanilhaNumerica(WritableSheet sheet) throws WriteException {
//		WritableFont font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
//		WritableCellFormat format = new WritableCellFormat(font);
//
//		CellView cv0 = sheet.getColumnView(0);
//		cv0.setSize(13 * 256);
//		sheet.setColumnView(0, cv0);
//		Label cabecalho0 = new Label(0, 0, "Numero", format);
//		sheet.addCell(cabecalho0);
//
//		CellView cv1 = sheet.getColumnView(1);
//		cv1.setSize(41 * 256);
//		sheet.setColumnView(1, cv1);
//		Label cabecalho1 = new Label(1, 0, "Responsável", format);
//		sheet.addCell(cabecalho1);
//
//		CellView cv2 = sheet.getColumnView(2);
//		cv2.setSize(13 * 256);
//		sheet.setColumnView(2, cv2);
//		Label cabecalho2 = new Label(2, 0, "Excedentes", format);
//		sheet.addCell(cabecalho2);
//
//		CellView cv3 = sheet.getColumnView(3);
//		cv3.setSize(10 * 256);
//		sheet.setColumnView(3, cv3);
//		Label cabecalho3 = new Label(3, 0, "Minutos", format);
//		sheet.addCell(cabecalho3);
//
//		CellView cv4 = sheet.getColumnView(4);
//		cv4.setSize(16 * 256);
//		sheet.setColumnView(4, cv4);
//		Label cabecalho4 = new Label(4, 0, "PlanoNormal Internet", format);
//		sheet.addCell(cabecalho4);
//
//		CellView cv5 = sheet.getColumnView(5);
//		cv5.setSize(21 * 256);
//		sheet.setColumnView(5, cv5);
//		Label cabecalho5 = new Label(5, 0, "Pacote Mensagem", format);
//		sheet.addCell(cabecalho5);
//
//		CellView cv6 = sheet.getColumnView(6);
//		cv6.setSize(16 * 256);
//		sheet.setColumnView(6, cv6);
//		Label cabecalho6 = new Label(6, 0, "INTRA-REDE", format);
//		sheet.addCell(cabecalho6);
//
//		CellView cv7 = sheet.getColumnView(7);
//		cv7.setSize(9 * 256);
//		sheet.setColumnView(7, cv7);
//		Label cabecalho7 = new Label(7, 0, "Total", format);
//		sheet.addCell(cabecalho7);
//	}

	// public void gerarPlanilhaAlfabetica(List<Fatura> faturas) throws
	// IOException, WriteException {
	//
	// Collections.sort(faturas, new Comparator<Fatura>() {
	// @Override
	// public int compare(Fatura f1, Fatura f2) {
	//
	// return f1.getNome().compareTo(f2.getNome());
	// }
	// });
	//
	// String mes = getMes();
	//
	// WritableWorkbook workbook = Workbook.createWorkbook(new File(dir,
	// "Planilha Alfabetica " + mes + ".xls"));
	//
	// WritableSheet sheet = workbook.createSheet("Planilha Alfabetica", 0);
	//
	// configurarCabecalhoPlanilhaAlfabetica(sheet);
	//
	// Map map = gerarTotalPorCliente(faturas);
	//
	// Fatura f = new Fatura();
	// f.setNome(" ");
	// f.setNumero(" ");
	// f.setTotal(BigDecimal.ZERO);
	// faturas.add(faturas.size(), f);
	//
	// int linha = 1;
	// int ultimoIndex = faturas.size() - 1;
	// int linhaClienteAtual = 0;
	// WritableCellFormat cellFormat = new WritableCellFormat();
	// cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
	// for (int i = 0; i < faturas.size(); i++) {
	// if (!faturas.get(i).getNome().equals("")) {
	// Label nome = new Label(1, linha, faturas.get(i).getNome());
	// sheet.addCell(nome);
	//
	// Label numero = new Label(0, linha, faturas.get(i).getNumero());
	// sheet.addCell(numero);
	//
	// if (i == ultimoIndex) {
	// Label valor = new Label(2, linha, " ");
	// sheet.addCell(valor);
	// } else {
	// Label valor = new Label(2, linha, "R$ " +
	// faturas.get(i).getTotal().toString());
	// sheet.addCell(valor);
	// }
	//
	// if (i < ultimoIndex) {
	// if (faturas.get(i).getNome().equals(faturas.get(i + 1).getNome())) {
	// if (i == 0) {
	// linhaClienteAtual = linha;
	// }
	// if (i > 0 && !faturas.get(i).getNome().equals(faturas.get(i -
	// 1).getNome())) {
	// linhaClienteAtual = linha;
	// }
	// Label total = new Label(3, linha, "R$ " +
	// map.get(faturas.get(i).getNome()), cellFormat);
	// sheet.addCell(total);
	// } else {
	// if (linhaClienteAtual != 0) {
	// sheet.mergeCells(3, linhaClienteAtual, 3, linha);
	// }
	// Label total = new Label(3, linha, "R$ " +
	// map.get(faturas.get(i).getNome()));
	// sheet.addCell(total);
	// linhaClienteAtual = 0;
	// }
	// } else {
	// Label total = new Label(3, linha, " ");
	// sheet.addCell(total);
	// }
	// linha++;
	// }
	// }
	// workbook.write();
	// workbook.close();
	// faturas.remove(faturas.size() - 1);
	// }
	//
	// private void configurarCabecalhoPlanilhaAlfabetica(WritableSheet sheet)
	// throws WriteException {
	// WritableFont font = new WritableFont(WritableFont.TIMES, 12,
	// WritableFont.BOLD);
	// WritableCellFormat format = new WritableCellFormat(font);
	//
	// CellView cv0 = sheet.getColumnView(0);
	// cv0.setSize(13 * 256);
	// sheet.setColumnView(0, cv0);
	// Label cabecalho0 = new Label(0, 0, "Numero", format);
	// sheet.addCell(cabecalho0);
	//
	// CellView cv1 = sheet.getColumnView(1);
	// cv1.setSize(41 * 256);
	// sheet.setColumnView(1, cv1);
	// Label cabecalho1 = new Label(1, 0, "Nome", format);
	// sheet.addCell(cabecalho1);
	//
	// CellView cv2 = sheet.getColumnView(2);
	// cv2.setSize(11 * 256);
	// sheet.setColumnView(2, cv2);
	// Label cabecalho2 = new Label(2, 0, "Valor", format);
	// sheet.addCell(cabecalho2);
	//
	// CellView cv3 = sheet.getColumnView(3);
	// cv3.setSize(9 * 256);
	// sheet.setColumnView(3, cv3);
	// Label cabecalho3 = new Label(3, 0, "Total", format);
	// sheet.addCell(cabecalho3);
	// }

//	private Map gerarTotalPorCliente(List<Fatura> faturas) {
//		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
//		for (Fatura f : faturas) {
//			if (!f.getNome().trim().isEmpty()) {
//				if (!map.containsKey(f.getNome())) {
//					map.put(f.getNome(), f.getTotal());
//				} else {
//					BigDecimal total = new BigDecimal("0.00");
//					total = total.add(map.get(f.getNome()));
//					total = total.add(f.getTotal());
//					map.put(f.getNome(), total);
//				}
//			}
//		}
		// TreeMap sortedHashMap = new TreeMap(map);
		// Iterator<String> keySetIterator = sortedHashMap.keySet().iterator();
		//
		// while (keySetIterator.hasNext()) {
		// String key = keySetIterator.next();
		// System.out.println("key: " + key + " value: " +
		// sortedHashMap.get(key));
		// }
//		return map;
//	}

	

}
