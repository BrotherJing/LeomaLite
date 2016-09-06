package com.example;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by jingyanga on 2016/9/6.
 */
@AutoService(Processor.class)
public class LeomaHandlerProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Elements mElementUtils;
    private Messager mMessager;

    private Map<String, LeomaAnnotatedClass> mAnnotatedClassMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(LeomaApi.class.getCanonicalName());
        types.add(LeomaURL.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mAnnotatedClassMap.clear();
        processLeomaApi(roundEnv);
        processLeomaURL(roundEnv);
        return true;
    }

    private void processLeomaURL(RoundEnvironment roundEnv){
        for(Element element : roundEnv.getElementsAnnotatedWith(LeomaURL.class)){
            LeomaAnnotatedClass annotatedClass = getAnnotatedClass(element);
            LeomaURLMethod method = new LeomaURLMethod(element);
            annotatedClass.addLeomaURLMethod(method);
        }
        FieldSpec hashMapField = FieldSpec.builder(ParameterizedTypeName.get(TypeUtils.HASHMAP, TypeUtils.STRING, TypeUtils.LEOMA_URL_HANDLER),
                "map", Modifier.PUBLIC, Modifier.STATIC).build();

        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        methodSpecBuilder.addStatement("map = new $T()", ParameterizedTypeName.get(TypeUtils.HASHMAP, TypeUtils.STRING, TypeUtils.LEOMA_URL_HANDLER));

        for(LeomaAnnotatedClass annotatedClass:mAnnotatedClassMap.values()){
            annotatedClass.addLeomaURLHandlers("map",methodSpecBuilder);
        }

        TypeSpec handlerTableClass = TypeSpec.classBuilder(TypeUtils.URL_HANDLER_TABLE)
                .addModifiers(Modifier.PUBLIC)
                .addField(hashMapField)
                .addMethod(methodSpecBuilder.build())
                .build();

        String packageName = TypeUtils.URL_HANDLER_TABLE.packageName();

        JavaFile file = JavaFile.builder(packageName, handlerTableClass).build();
        System.out.println(file.toString());
        try {
            file.writeTo(mFiler);
            info("generating file for %s", TypeUtils.URL_HANDLER_TABLE.toString());
        } catch (IOException e) {
            info("generating file failed, %s", e.getMessage());
        }
    }

    private void processLeomaApi(RoundEnvironment roundEnv){
        for(Element element : roundEnv.getElementsAnnotatedWith(LeomaApi.class)){
            LeomaAnnotatedClass annotatedClass = getAnnotatedClass(element);
            LeomaApiMethod method = new LeomaApiMethod(element);
            annotatedClass.addLeomaApiMethod(method);
        }
        FieldSpec hashMapField = FieldSpec.builder(ParameterizedTypeName.get(TypeUtils.HASHMAP, TypeUtils.STRING, TypeUtils.LEOMA_API_HANDLER),
                "map", Modifier.PUBLIC, Modifier.STATIC).build();

        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        methodSpecBuilder.addStatement("map = new $T()", ParameterizedTypeName.get(TypeUtils.HASHMAP, TypeUtils.STRING, TypeUtils.LEOMA_API_HANDLER));

        for(LeomaAnnotatedClass annotatedClass:mAnnotatedClassMap.values()){
            annotatedClass.addLeomaApiHandlers("map",methodSpecBuilder);
        }

        TypeSpec handlerTableClass = TypeSpec.classBuilder(TypeUtils.API_HANDLER_TABLE)
                .addModifiers(Modifier.PUBLIC)
                .addField(hashMapField)
                .addMethod(methodSpecBuilder.build())
                .build();

        String packageName = TypeUtils.API_HANDLER_TABLE.packageName();

        JavaFile file = JavaFile.builder(packageName, handlerTableClass).build();
        System.out.println(file.toString());
        try {
            file.writeTo(mFiler);
            info("generating file for %s", TypeUtils.API_HANDLER_TABLE.toString());
        } catch (IOException e) {
            info("generating file failed, %s", e.getMessage());
        }
    }

    private LeomaAnnotatedClass getAnnotatedClass(Element element){
        TypeElement classElement = (TypeElement)element.getEnclosingElement();
        String fullName = classElement.getQualifiedName().toString();
        LeomaAnnotatedClass annotatedClass = mAnnotatedClassMap.get(fullName);
        if(annotatedClass==null){
            annotatedClass = new LeomaAnnotatedClass(classElement, mElementUtils);
            mAnnotatedClassMap.put(fullName, annotatedClass);
        }
        return annotatedClass;
    }

    private void info(String msg, Object... args){
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg,args));
        System.out.println(String.format(msg,args));
    }
}
