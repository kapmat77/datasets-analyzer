package mk.datasets.app;

/**
 * Created by Kapmat on 2016-05-28.
 */
public class Event {

	private int id;
	private String expression;

	enum Mark {
		OR,
		AND
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
}
