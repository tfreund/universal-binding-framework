import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTextField;

import de.freund.syncer.Syncer;
import de.freund.syncer.transformer.DateTransformer;
import de.freund.syncer.SyncTransaction;

public class Tester {

	private static JTextField tf;
	private static JLabel errorLabel;

	public static void main(String[] args) {

		Person person = new Person();
		person.setBirthday(new Date());

		tf = new JTextField();
		errorLabel = new JLabel();

		Syncer<Person> syncer = new Syncer<Person>();

		DateTransformer dt = new DateTransformer();

		SyncTransaction syncTransaction = new SyncTransaction<Person, Date, String>(Person::getBirthday, Person::setBirthday, tf::getText, tf::setText,
				dt::dateToString, dt::stringToDate, (e) -> errorLabel.setText(e.getMessage()));

		syncer.addSyncTransaction(syncTransaction);

		syncer.setObject(person);

		System.out.println(tf.getText());
		
		tf.setText("01.02.2003");
		
		syncer.writeUiToBO();
		System.out.println("Label: " + errorLabel.getText());
	}


}
