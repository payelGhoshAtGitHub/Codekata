package exception;

public class CodeKataException extends Exception {

  private static final long serialVersionUID = 1L;

  public CodeKataException(String message) {
    super(message);
  }

  public CodeKataException(String message, Throwable cause) {
    super(message, cause);
  }
}
