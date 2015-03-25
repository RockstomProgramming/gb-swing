package br.com.finan.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public final class JasperUtil {

	public static JasperViewer gerarRelatorio(final String arquivo, final List<?> dados, final Map<String, Object> params) {
		try {
			final URL resource = JasperUtil.class.getResource("/relatorios/".concat(arquivo));
			final InputStream in = new FileInputStream(resource.getFile());
			final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados);
			final JasperPrint print = JasperFillManager.fillReport(in, params, dataSource);
			final JasperViewer viewer = new JasperViewer(print, false);
			return viewer;
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
