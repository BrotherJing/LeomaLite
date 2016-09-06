package com.example;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
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
 * Created by jingyanga on 2016/9/5.
 */

@AutoService(Processor.class)
public class LeomaProcessor extends AbstractProcessor{

    private Filer mFiler;
    private Elements mElementUtils;
    private Messager mMessager;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processLeomaFinder(roundEnv);
        return true;
    }

    private void processLeomaFinder(RoundEnvironment roundEnv){
        for(Element element : roundEnv.getElementsAnnotatedWith(LeomaFinder.class)){
            TypeElement leomaClassElement = (TypeElement)element;

            MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder("inject")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(TypeName.get(leomaClassElement.asType()),"host");

            TypeSpec leomaApiFinder = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(TypeUtils.LEOMA_API_FINDER)
                    .addMethod(MethodSpec.methodBuilder("getApiHandler")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeUtils.LEOMA_API_HANDLER)
                        .addParameter(TypeUtils.STRING, "name")
                        .addStatement("return $T.map.get(name)", TypeUtils.HANDLER_TABLE).build())
                    .build();

            injectMethodBuilder.addStatement("$T.inject()", TypeUtils.HANDLER_TABLE);

            injectMethodBuilder.addStatement("$T.getInstance().leomaApiFinder = $L", TypeUtils.LEOMA, leomaApiFinder);

            TypeSpec innerInjectorClass = TypeSpec.classBuilder(leomaClassElement.getSimpleName()+"$$InnerInjector")
                    .addSuperinterface(ParameterizedTypeName.get(TypeUtils.INNER_INJECTOR,TypeName.get(leomaClassElement.asType())))
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(injectMethodBuilder.build())
                    .build();

            String packageName = mElementUtils.getPackageOf(leomaClassElement)
                    .getQualifiedName().toString();

            System.out.println("package name is: "+packageName);

            JavaFile file = JavaFile.builder(packageName, innerInjectorClass).build();
            System.out.println(file.toString());
            try {
                file.writeTo(mFiler);
                info("generating file for %s", TypeUtils.HANDLER_TABLE.toString());
            } catch (IOException e) {
                info("generating file failed, %s", e.getMessage());
            }
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(LeomaFinder.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
    }

    private void info(String msg, Object... args){
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg,args));
        System.out.println(String.format(msg,args));
    }
}
