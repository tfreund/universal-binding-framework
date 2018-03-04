import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTransformer {
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

	public String dateToString(Date date) {
		return simpleDateFormat.format(date);
	}

	public Date stringToDae(String dateString) throws ParseException {
		return simpleDateFormat.parse(dateString);
	}
}
