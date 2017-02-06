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
    private String fluentPrefix;
    private boolean generateSetter;
    private boolean generateGetter;
    private boolean generateFluent;
    private Editor editor;


    public FluentAccessorsWorker(final Project project, final Editor editor, final PsiClass clazz,
                                         final String setterPrefix,
                                         final String getterPrefix,
                                         final String fluentPrefix,
                                         final boolean generateSetter,
                                         final boolean generateGetter,
                                         final boolean generateFluent) {
        this.elementFactory = JavaPsiFacade.getElementFactory(project);
        this.editor = editor;
        this.psiClass = clazz;
        this.codeStyleManager = CodeStyleManager.getInstance(project);
        this.setterPrefix = setterPrefix;
        this.getterPrefix = getterPrefix;
        this.fluentPrefix = fluentPrefix;
        this.generateSetter = generateSetter;
        this.generateGetter = generateGetter;
        this.generateFluent = generateFluent;
    }

    public void execute(final PsiField[] candidateFields) {

        FiMethodTester tester = new FiMethodTester(psiClass);

        for (PsiField field : candidateFields) {

            if (generateSetter && !(tester.hasSetter(field, setterPrefix))) {
                generateSetterMethod(field);
            }
            if (generateGetter && !(tester.hasGetter(field, getterPrefix))) {
                generateGetterMethod(field);
            }
            if (generateFluent && !(tester.hasFluent(field, fluentPrefix))) {
                generateFluentMethod(field);
            }
        }
    }

    private void generateSetterMethod(final PsiField candidateField) {
        createMethodFromText(buildSetterMethodText(candidateField));
    }

    private void generateGetterMethod(final PsiField candidateField) {
        createMethodFromText(buildGetterMethodText(candidateField));
    }

    private void generateFluentMethod(final PsiField candidateField) {
        createMethodFromText(buildFluentMethodText(candidateField));
    }

    private String buildSetterMethodText(final PsiField candidateField) {

        return "public void " + constructMethodName(candidateField, setterPrefix) +
                " (" + candidateField.getType().getCanonicalText() + " " +
                candidateField.getName() + ") {\n" +
                "\tthis." + candidateField.getName() + " = " +
                candidateField.getName() + ";\n}";

    }

    private String buildGetterMethodText(final PsiField candidateField) {

        String prefix = "";

        if (candidateField.getType().isAssignableFrom(PsiType.BOOLEAN)) {
            prefix = "is";
        } else {
            prefix = getterPrefix;
        }

        return "public " + candidateField.getType().getCanonicalText() + " " +
                constructMethodName(candidateField, prefix) + " () {\n" +
                "\treturn this." + candidateField.getName() + ";\n}";

    }

    private String buildFluentMethodText(final PsiField candidateField) {

        return "public " + psiClass.getName() + " " +
                constructMethodName(candidateField, fluentPrefix) + " (" +
                candidateField.getType().getCanonicalText() + " " +
                candidateField.getName() + ") {\n" +
                "\tthis." + candidateField.getName() + " = " +
                candidateField.getName() + ";\n" + "\treturn this;\n}";
    }

    private String constructMethodName(final PsiField candidateField, String prefix) {
        final String fieldName = candidateField.getName();
        if (prefix.equals("")) {
            return fieldName;
        } else {
            return prefix + StringUtils.capitalizeFirstLetter(fieldName);
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
}
