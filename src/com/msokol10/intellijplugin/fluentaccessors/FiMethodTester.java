package com.msokol10.intellijplugin.fluentaccessors;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.*;


class FiMethodTester {
    private PsiMethod[] classPsiMethods;
    private PsiClass psiClass;

    FiMethodTester(final PsiClass psiClass) {
        this.psiClass = psiClass;
        this.classPsiMethods = psiClass.getMethods();
    }

    boolean hasReadWriteMethod (final PsiField candidateField) {
        String fieldName = candidateField.getName();
        boolean setterMethodFound = false;
        boolean getterMethodFound = false;
        boolean fluentMethodFound = false;

        for (PsiMethod method : classPsiMethods) {
            if (isSameNamePublicNonFinalInstanceMethod(fieldName, method)) {
                if (isSetter(method, candidateField.getType())) {
                    setterMethodFound = true;
                } else if (isGetter(method, candidateField.getType())) {
                    getterMethodFound = true;
                } else if (isFluent(method, candidateField.getType())) {
                    fluentMethodFound = true;
                }
            }
        }
        return setterMethodFound && getterMethodFound && fluentMethodFound;
    }

    boolean hasSetter(final PsiField candidateField, String setterPrefix) {

        String methodName = "";

        if (setterPrefix.equals("")) {
            methodName = candidateField.getName();
        } else {
            methodName = setterPrefix + StringUtils.capitalizeFirstLetter(candidateField.getName());
        }

        for (PsiMethod method : classPsiMethods) {
            if (isSameNamePublicNonFinalInstanceMethod(methodName, method)) {
               return isSetter(method, candidateField.getType());
            }
        }
        return false;
    }

    boolean hasGetter(final PsiField candidateField, String getterPrefix) {

        String methodName = "";

        if (getterPrefix.equals("")) {
            methodName = candidateField.getName();
        } else {
            if (candidateField.getType().isAssignableFrom(PsiType.BOOLEAN)) {
                methodName = "is" + StringUtils.capitalizeFirstLetter(candidateField.getName());
            } else {
                methodName = getterPrefix + StringUtils.capitalizeFirstLetter(candidateField.getName());
            }
        }

        for (PsiMethod method : classPsiMethods) {
            if (isSameNamePublicNonFinalInstanceMethod(methodName, method)) {
                return isGetter(method, candidateField.getType());
            }
        }
        return false;
    }

    boolean hasFluent(final PsiField candidateField, String fluentPrefix) {

        String methodName = "";

        if (fluentPrefix.equals("")) {
            methodName = candidateField.getName();
        } else {
            methodName = fluentPrefix + StringUtils.capitalizeFirstLetter(candidateField.getName());
        }

        for (PsiMethod method : classPsiMethods) {
            if (isSameNamePublicNonFinalInstanceMethod(methodName, method)) {
                return isFluent(method, candidateField.getType());
            }
        }
        return false;
    }

    private boolean isSetter(final PsiMethod method, final PsiType fieldType) {
        PsiType returnType = method.getReturnType();

        if (returnType == null) {
            PsiParameterList psiParameterList = method.getParameterList();
            PsiParameter[] psiParameter = psiParameterList.getParameters();
            return psiParameter.length == 1 && psiParameter[0].getType().equals(fieldType);
        } else {
            return false;
        }
    }

    private boolean isGetter(final PsiMethod method, final PsiType fieldType) {
        return fieldType.equals(method.getReturnType()) && method.getParameterList().getParametersCount() == 0;
    }

    private boolean isFluent(final PsiMethod method, final PsiType fieldType) {
        PsiType returnType = method.getReturnType();

        if (returnType == null) {
            return false;
        }

        if (!(returnType.getCanonicalText().equals(psiClass.getQualifiedName()))) {
            return false;
        }

        PsiParameterList psiParameterList = method.getParameterList();
        PsiParameter[] psiParameter = psiParameterList.getParameters();
        return psiParameter.length == 1 && psiParameter[0].getType().equals(fieldType);
    }

    private boolean isSameNamePublicNonFinalInstanceMethod(final String fieldName, final PsiMethod method) {

        FluentAccessorsApplicationComponent applicationComponent = ApplicationManager.getApplication()
                .getComponent(FluentAccessorsApplicationComponent.class);

        String setterPrefix = applicationComponent.getSetterPrefix();
        String getterPrefix = applicationComponent.getGetterPrefix();
        String fluentPrefix = applicationComponent.getFluentPrefix();

        if ((method.getName().equals(setterPrefix + fieldName)) ||
                (method.getName().equals(getterPrefix + fieldName)) ||
                (method.getName().equals(fluentPrefix + fieldName))) {
            if (method.hasModifierProperty(PsiModifier.PUBLIC)) {
                if (!(method.hasModifierProperty(PsiModifier.FINAL))) {
                    if (!(method.hasModifierProperty(PsiModifier.STATIC))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
