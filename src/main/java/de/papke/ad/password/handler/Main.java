package de.papke.ad.password.handler;

import java.io.File;

import javax.swing.UIManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import de.papke.ad.password.handler.gui.PasswordChangeForm;

public class Main {

	public static void main(String[] args) throws Exception {
		
		// create the Options
		Options options = new Options();
		options.addOption("c", "config-dir", true, "Config directory with smb credentials and ad configuration\n[default: ~/.ad-password-handler]");
		options.addOption("d", "days-till-warning", true, "Days until password expiration to show warning\n[default: 14]");
		options.addOption("h", "help", false, "Print this help text");

		try {

			// create the command line parser
			CommandLineParser parser = new PosixParser();

			// parse the command line arguments
			CommandLine line = parser.parse(options, args);

			// get the value for the config dir
			String configDirPath = line.getOptionValue("c");
			if (configDirPath == null) {
				File userHomeDir = new File(System.getProperty("user.home"));
				configDirPath = userHomeDir.getAbsolutePath() + "/." + Constants.APP_NAME;
			}
			
			// get the value for days till warning
			String daysTillWarningString = line.getOptionValue("d");
			int daysTillWarning = 14;
			if (daysTillWarningString != null) {
				daysTillWarning = Integer.parseInt(daysTillWarningString);
			}

			// check if mandatory parameters are set
			if (!line.hasOption("help")) {
				
				// read the config dir
				File configDir = new File(configDirPath);
				
				// will the password expire?
				PasswordExpirationHandler passwordExpirationHandler = new PasswordExpirationHandler(configDir);
				int daysTillPasswordExpires = passwordExpirationHandler.getDaysTillPasswordExpires();

				// could the days till password expires be retrieved?
				if (daysTillPasswordExpires != -1) {
				
					// yes -> show password warning
					if (daysTillPasswordExpires <= daysTillWarning) {
						
						// set native look and feel
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						
						// show popup with password change form
						PasswordChangeHandler passwordChangeHandler = new PasswordChangeHandler(configDir);
						PasswordChangeForm form = new PasswordChangeForm(passwordExpirationHandler, passwordChangeHandler);
						form.init();
					}
					else {
						// no -> show expiration information on console 
						System.out.println("Your password will expire in " + daysTillPasswordExpires + " days. No change is necessary.");
					}
				}
				else {
					System.out.println("There was a problem to get the days till password expiration.");
				}
			}
			else {
				printHelp(options);
			}
		}
		catch (Exception e) {
			printHelp(options);
		}
	}
	
	private static void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(Constants.APP_NAME, options);
	}
}
