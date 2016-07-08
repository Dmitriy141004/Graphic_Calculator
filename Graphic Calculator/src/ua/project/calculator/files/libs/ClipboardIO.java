/**
 * <h1><b>======= ClipboardIO =======</b></h1>
 *
 * Данный класс является утилитой для общения с буфером обмена.
 * Внимание! При чтении из буфера может возникать IOException или UnsupportedFlavorException!
 * Их надо учитывать!
 *
 * @author Дмитрий Мелешко
 * @since 1.5.3
 */

package ua.project.calculator.files.libs;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

public class ClipboardIO implements ClipboardOwner {          // К классу присобачен интерфейс для общения с буфером
    StringSelection stringSelection;

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }

    /** <h1><b> Метод для записи в буфер обмена </b></h1>
     * <p> Заметьте, при записи надо брать не <tt>String</tt> значение,
     * а <tt>StringSelection</tt>. Поэтому в начале <tt>String</tt> переобразуется в
     * <tt>StringSelection</tt>. </p>
     *
     * @param text текст, который будет записан в буфер.
     * @see ClipboardIO#getText()
     */
    public void setText(String text){
        stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
    }

    /** <h1><b> Метод для чтения из буфера обмена </b></h1>
     * <p> Хочу напомнить, метод вызывает исключения,
     * в коде их надо учитывать:
     * </p>
     *
     * <blockquote><pre>
     *     ClipboardIO io = new ClipboardIO();
     *     try {
     *         System.out.println(io.getText());    // Пробуем получить доступ
     *     } catch (IOException | UnsupportedFlavorException e1) {
     *         System.out.println("Ошибка при доступе");
     *     }
     * </blockquote></pre>
     *
     * @return текст, записаный в буфере.
     * @throws IOException ошибка в доступе
     * @throws UnsupportedFlavorException особенности запрашиваемой информации не поддерживается
     * @see ClipboardIO#setText(String)
     */
    public String getText() throws IOException, UnsupportedFlavorException {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        return (String) clipboard.getData(DataFlavor.stringFlavor);
    }
}
