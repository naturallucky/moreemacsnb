/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.*;
import org.netbeans.editor.BaseDocument;
import org.netbeans.lib.editor.util.swing.DocumentUtilities;

/**
 *
 * @author Yasuhiro Endoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-KillRegionAction")
public class KillRegionAction extends MoreEmacsAction {
    public KillRegionAction() {
        super("kill-region");
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        Caret caret = target.getCaret();
        ActionMap actionMap = target.getActionMap();
        if(actionMap == null)  {
            return;
        }
        Action cutAction = actionMap.get(DefaultEditorKit.cutAction);
         
        if(caret.getDot() != caret.getMark()) {
            cutAction.actionPerformed(e);
            return;
        }
        
        BaseDocument doc = (BaseDocument)target.getDocument();
        doc.runAtomicAsUser (() -> {
            try {
                DocumentUtilities.setTypingModification(doc, true);
                int mark = Mark.get(target);
                int dot = caret.getDot();
                caret.setDot(mark);
                caret.moveDot(dot);
                target.cut();
            } finally {
                DocumentUtilities.setTypingModification(doc, false);
            }
        });
    }
}

