package com.msokol10.intellijplugin.fluentaccessors;

import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import org.jetbrains.annotations.Nullable;


public class FluentAccessorsWorker {

    private PsiElementFactory elementFactory;
    private PsiClass psiClass;
    private CodeStyleManager codeStyleManager;
    private String setterPrefix;
    private String getterPrefix;
    private boolean generateGetter;
    private boolean invokeExistingSetters;
    private Editor editor;


    public FluentAccessorsWorker(final Project project,
                                         final Editor editor,
                                         final PsiClass clazz,
                                         final String setterPrefix,
                                         final String getterPrefix,
                                         final boolean generateGetter,
                                         final boolean invokeExistingSetters) {
        this.elementFactory = JavaPsiFacade.getElementFactory(project);
        this.editor = editor;
        this.psiClass = clazz;
        this.codeStyleManager = CodeStyleManager.getInstance(project);
        this.setterPrefix = setterPrefix;
        this.getterPrefix = getterPrefix;
        this.generateGetter = generateGetter;
        this.invokeExistingSetters = invokeExistingSetters;
    }

    public void execute(final PsiField[] candidateFields) {
        if (generateGetter) {
            generateGetterMethods(candidateFields);
        }

        generateSetterMethods(candidateFields);
    }

    private void generateSetterMethods(final PsiField[] candidateFields) {
        for (PsiField candidateField : candidateFields) {
            createMethodFromText(buildWriteMethodText(candidateField));
        }
    }

    private void generateGetterMethods(final PsiField[] candidateFields) {
        for (PsiField candidateField : candidateFields) {
            createMethodFromText(buildReadMethodText(candidateField));
        }
    }

    private String buildReadMethodText(final PsiField candidateField) {
        return "public " + candidateField.getType().getCanonicalText() + " "
                + constructGetterName(candidateField) + "() " +
                "{ " +
                "return this." + candidateField.getName() + ";" +
                "}";
    }

    private String constructGetterName(final PsiField candidateField) {
        final String fieldName = candidateField.getName();
        if (getterPrefix.equals("")) {
            return fieldName;
        } else {
            return getterPrefix + upperFirstLetter(fieldName);
        }
    }

    private void createMethodFromText(String methodText) {
        PsiMethod psiMethod = elementFactory.createMethodFromText(methodText, null);
        codeStyleManager.reformat(psiMethod);

        PsiElement element = findMatchingElement(psiClass.getContainingFile(), editor);
        psiClass.addAfter(psiMethod, element);
    }

    private boolean isValidElement (final PsiElement psiElement) {
        return psiElement.getParent() == null || psiElement instanceof PsiMethod || psiElement instanceof PsiField;
    }

    @Nullable
    private PsiElement findMatchingElement (PsiFile file,
                                            Editor editor) {
        final CaretModel caretModel = editor.getCaretModel();
        final int position = caretModel.getOffset();
        PsiElement element = file.findElementAt(position);
        while (element != null) {
            if (isValidElement(element)) {
                return element;
            } else {
                element = element.getPrevSibling();
            }
        }

        return file;

    }

    private String buildWriteMethodText(final PsiField candidateField) {
        String m = "public " + psiClass.getName() + " "
                + constructSetterName(candidateField) + "(" + "final " + candidateField.getType().getCanonicalText()
                + " " + candidateField.getName() + "){";
        if (invokeExistingSetters) {
            m += retrieveExistingSetterName(candidateField) + "(" + candidateField.getName() + ");";
        } else {
            m += "this." + candidateField.getName() + " = " + candidateField.getName() + ";";
        }
        m +=" return this; }";

        return m;
    }

    private String constructSetterName(final PsiField candidateField) {
        final String fieldName = candidateField.getName();
        if (setterPrefix.equals("")) {
            return fieldName;
        } else {
            return setterPrefix + upperFirstLetter(fieldName);
        }
    }

    /**
     * Returns the name of the existing setter method for the specified field.
     * Note: currently just the name is assembled as expected from the Java Bean standard
     * rather than finding the method reference in the containing PsiClass.
     */
    private String retrieveExistingSetterName(final PsiField candidateField) {
        String name = candidateField.getName();
        if (candidateField.getType().isAssignableFrom(PsiType.BOOLEAN) && name != null && name.startsWith("is")) {
            name = name.substring(2);
        }
        return "set" + upperFirstLetter(name);
    }

    private String upperFirstLetter(final String name) {
        char[] firstLetter = new char[]{name.charAt(0)};
        return new String(firstLetter).toUpperCase() + name.substring(1);
    }
}
