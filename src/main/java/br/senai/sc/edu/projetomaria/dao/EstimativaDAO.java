package br.senai.sc.edu.projetomaria.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstimativaDAO extends AbstractDAO {

	public List<Integer> listarSKU() {
		String sql = "SELECT SKU FROM PRODUTO WHERE SKU IN (SELECT SKU FROM HISTORICO WHERE MES_ANO > (SELECT DATE_ADD(SYSDATE(),INTERVAL -6 MONTH))) group by SKU;";
		List<Integer> lista_SKU = new ArrayList<>();

		try (Connection conn = getConnection();
				Statement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery(sql);) {

			while (rs.next()) {
				Integer sku;
				sku = rs.getInt("SKU");
				lista_SKU.add(sku);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista_SKU;
	}

	public List<Integer> listaHistorico(Integer sku) {
		String sql = "SELECT QUANTIDADE FROM HISTORICO WHERE SKU IN (SELECT SKU_PHASE_OUT FROM SKU_PHASE WHERE SKU_PHASE_IN = ?) OR SKU = ? ORDER BY MES_ANO ASC";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, sku);
			pstmt.setInt(2, sku);

			ResultSet rs = pstmt.executeQuery();

			List<Integer> lista_historico = new ArrayList<>();
			while (rs.next()) {

				Integer skuHistorico;
				skuHistorico = rs.getInt("QUANTIDADE");
				lista_historico.add(skuHistorico);
			}
			return lista_historico;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
