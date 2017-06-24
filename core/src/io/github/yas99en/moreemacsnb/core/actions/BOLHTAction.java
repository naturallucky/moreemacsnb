/*
 * Copyright (c) 2017, Toshikazu Ito
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of the authors nor the names of its contributors may be
 *     used to endorse or promote products derived from this software without
 *     specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import io.github.yas99en.moreemacsnb.core.utils.CodePointIterator;
import io.github.yas99en.moreemacsnb.core.utils.DocumentCharSequence;
import java.awt.event.ActionEvent;
import static java.time.Clock.offset;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;
import org.netbeans.editor.BaseDocument;
/**
 *
 * @author  Toshikazu Ito
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-BOLHTAction")
public class BOLHTAction extends MoreEmacsAction {
    public BOLHTAction() {
        super("bol-ht");
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        try {
            doActionPerformed(e, target);
        } catch (BadLocationException ex) {
            throw new AssertionError(ex.getMessage(), ex);
        }
    }

    //if column = 0, skip blank
    public void doActionPerformed(ActionEvent e, JTextComponent target) throws BadLocationException {
        if(!target.isEditable() || !target.isEnabled()) {
            target.getToolkit().beep();
            return;
        }
       
        Caret caret = target.getCaret();
        int current = caret.getDot();

        BaseDocument doc = (BaseDocument)target.getDocument();
        Element rootElem = doc.getDefaultRootElement();
        int linePos = rootElem.getElementIndex(current);
        Element line = rootElem.getElement(linePos);

        int colum = current - line.getStartOffset();

        if (colum == 0){
            DocumentCharSequence seq = new DocumentCharSequence(doc);
            int offset = 0;
            String fc;
            int len = 32;
            int stage;
            for (stage = 0; stage < 50; stage ++) {
                offset = 0;
                fc = doc.getText(current + len * stage, len);
                while (offset < len && isBlank(fc.charAt(offset))){
                    offset++;
                }
                if (offset != len){
                    break;
                }
            }
            //caret.moveDot
            caret.setDot(current-colum + stage * len + offset);
            
        }else{
            caret.setDot(current-colum);
        }
    }
    
    private boolean isBlank(char ch){
        //return !Character.isLetterOrDigit(ch);
        return Character.isSpaceChar(ch) || Character.isWhitespace(ch);
    }
    
}
