package swiftbeam.processor;

import swiftbeam.annotations.MongoEntity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.samskivert.mustache.Template;
import restx.common.Mustaches;
import restx.common.processor.RestxAbstractProcessor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes({"swiftbeam.annotations.MongoEntity"})
@SupportedOptions({"debug"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class CollectionNameAnnotationProcessor extends RestxAbstractProcessor {

    private Template classCollectionTemplate;
    private Map<String, String> classCollections;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        classCollectionTemplate = Mustaches.compile(CollectionNameAnnotationProcessor.class,
                                                    "ClassCollections.mustache");
        classCollections = new HashMap<>();
    }

    @Override
    protected boolean processImpl(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws Exception {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(MongoEntity.class);

        if (elements.isEmpty()) {
            return false;
        }

        elements.forEach(element -> classCollections.put(((TypeElement) element).getQualifiedName().toString(),
                                                         element.getAnnotation(MongoEntity.class).value()));

        generateJavaClass("swiftbeam.utils.ClassCollections", classCollectionTemplate,
                          ImmutableMap.<String, Object>of("classCollections", classCollections.entrySet()),
                          Sets.newHashSet());
        return true;
    }
}
