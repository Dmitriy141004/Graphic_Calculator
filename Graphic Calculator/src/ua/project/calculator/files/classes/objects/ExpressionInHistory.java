package ua.project.calculator.files.classes.objects;

public class ExpressionInHistory {
    public String expression;
    public String result;
    public String date;

    public ExpressionInHistory(String result, String expression, String date) {
        this.result = result;
        this.expression = expression;
        this.date = date;
    }
}
