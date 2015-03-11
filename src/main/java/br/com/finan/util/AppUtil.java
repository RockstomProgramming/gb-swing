package br.com.finan.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 *
 * @author Wesley Luiz
 */
public final class AppUtil {

	private static final String PATH_CONFIG = "/META-INF/config.properties";
	public static final String MSG_SALVAR_SUCESSO = "Os dados foram salvos com sucesso!";
	public static final String PROP_CONF_FILECHOOSER = "prop.conf.filechooser";
	public static final String MODAL_INFO = "Informação";

	public static void exibirMsgSalvarSucesso(JComponent comp) {
		JOptionPane.showMessageDialog(comp, AppUtil.MSG_SALVAR_SUCESSO, AppUtil.MODAL_INFO, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static Properties getPropertyConfig(Class<?> clazz) {
		Properties prop = new Properties();

		try {
			File file = new File(getPathFileConfig(clazz));
			prop.load(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return prop;
	}
	
	public static void setPropertyConfig(Class<?> clazz, String value) {
		Properties prop = getPropertyConfig(clazz);
		prop.setProperty(PROP_CONF_FILECHOOSER, value);
		
		try {
			FileOutputStream out = new FileOutputStream(new File(getPathFileConfig(clazz)));
			prop.store(out, "");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getPathFileConfig(Class<?> clazz) {
		return clazz.getResource(PATH_CONFIG).getFile();
	}
}
