package cn.exception;

import net.lenni0451.classtransform.TransformerManager;
import net.lenni0451.classtransform.mixinstranslator.MixinsTranslator;
import net.lenni0451.classtransform.utils.tree.BasicClassProvider;

import java.lang.instrument.Instrumentation;

public class AgentMain {
    public static TransformerManager tsm = new TransformerManager(new BasicClassProvider());
    public static void agentmain(String args, Instrumentation instrumentation) throws Throwable {
        tsm.addTransformerPreprocessor(new MixinsTranslator());
        tsm.hookInstrumentation(instrumentation, true);
        Exception.instance.isFromAgent = true;
    }
}
