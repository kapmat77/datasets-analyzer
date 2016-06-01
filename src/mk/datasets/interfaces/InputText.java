package mk.datasets.interfaces;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Kapmat on 2016-06-01.
 */
public interface InputText {

	String getName();
	List<LocalDateTime> getDates();
	String getExpression();
}
