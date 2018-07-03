package br.senai.sc.edu.projetomaria.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import br.senai.sc.edu.projetomaria.model.Resultado;
import br.senai.sc.edu.projetomaria.resource.Messages;

public class EstimativaWritter {
	private static final Logger LOGGER = LogManager.getLogger();

	public void escrever(Path path, int periodo) {

		try (BufferedWriter arquivo = Files.newBufferedWriter(path);
				CSVPrinter escrever = new CSVPrinter(arquivo,
						CSVFormat.DEFAULT.withHeader("SKU", "PERIODO TOTAL", "RESULTADO 1", "RESULTADO 2",
								"RESULTADO 3", "RESULTADO 4", "ERRO QUADRATICO", "PERIODO UTILIZADO"));) {
			Estimativa estimativa = new Estimativa();
			String periodoEstimativa = "";

			for (Resultado periodoUti : estimativa.Calculo(periodo)) {
				for (Integer i : periodoUti.getPeriodo_utilizado()) {
					periodoEstimativa += i + ",";
				}
			}

			for (Resultado linha : estimativa.Calculo(periodo)) {
				escrever.printRecord(linha.getSKU(), linha.getPeriodo_total(), linha.getResultado_media(),
						linha.getResultado_media2(), linha.getResultado_media3(), linha.getResultado_media4(),
						linha.getErro_quadratico_medio(), periodoEstimativa);
			}
			LOGGER.info(Messages.ARQUIVO_GERADO);
		} catch (IOException ex) {
			LOGGER.info(Messages.ERRO_ARQUIVO);
			LOGGER.debug(ex);
		}
	}
}
