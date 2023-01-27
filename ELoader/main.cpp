#include "main.h"
#include "jni.h"
#include "jvmti.h"
#include "classes.h"
#include "loader.h"

VOID OutputLastError(DWORD errorCode)
{
    CHAR errorString[256] = {0};
    sprintf(errorString, "Last error code: %lu", errorCode);
    MessageBox(NULL, errorString, "ELoader", MB_OK | MB_ICONEXCLAMATION);
}

DWORD WINAPI MainThread(CONST LPVOID lpParam)
{
    MessageBoxA(NULL, "Starting injecting", "ELoader", MB_OK | MB_ICONINFORMATION);
    HMODULE jvmDll = GetModuleHandleA("jvm.dll");
    if (!jvmDll)
    {
        DWORD lastError = GetLastError();
        MessageBoxA(NULL, "Can't find jvm.dll module handle", "ELoader", MB_OK | MB_ICONERROR);
        OutputLastError(lastError);
        ExitThread(0);
    }
    FARPROC getJvmsVoidPtr = GetProcAddress(jvmDll, "JNI_GetCreatedJavaVMs");
    if (!getJvmsVoidPtr)
    {
        DWORD lastError = GetLastError();
        MessageBoxA(NULL, "Can't find JNI_GetCreatedJavaVMs proc handle", "ELoader", MB_OK | MB_ICONERROR);
        OutputLastError(lastError);
        ExitThread(0);
    }


    typedef jint (JNICALL * GetCreatedJavaVMs)(JavaVM **, jsize, jsize *);
    GetCreatedJavaVMs jni_GetCreatedJavaVMs = (GetCreatedJavaVMs)getJvmsVoidPtr;
    jsize nVMs;
	jni_GetCreatedJavaVMs(NULL, 0, &nVMs);
	JavaVM **buffer = new JavaVM *[nVMs];
	jni_GetCreatedJavaVMs(buffer, nVMs, &nVMs);
	if (nVMs == 0)
    {
        MessageBoxA(NULL, "JVM not found!", "ELoader", MB_OK | MB_ICONERROR);
        ExitThread(0);
	}
    if (nVMs > 0)
    {
        for (jsize i = 0; i < nVMs; i++)
        {
            JavaVM* jvm = buffer[i];
			JNIEnv* jniEnv = NULL;
			jvmtiEnv* jvmTiEnv = NULL;
            jvm->AttachCurrentThread((void **)(&jniEnv), 0);
            jvm->GetEnv((void **)(&jniEnv), JNI_VERSION_1_8);
            if (!jniEnv)
            {
                MessageBoxA(NULL, "Can't attach to JNIEnv", "ELoader", MB_OK | MB_ICONERROR);
                jvm->DetachCurrentThread();
                break;
            }
            jclass classLoaderClazz = NULL;

            classLoaderClazz = jniEnv->DefineClass(NULL, NULL, (jbyte*)classLoaderClass, classLoaderClassSize);

            if (!classLoaderClazz)
            {
                MessageBoxA(NULL, "Error on find Loader", "ELoader EDIT", MB_OK | MB_ICONERROR);
                jvm->DetachCurrentThread();
                break;
            }
            jobjectArray classesData = (jobjectArray)jniEnv->CallStaticObjectMethod(classLoaderClazz, jniEnv->GetStaticMethodID(classLoaderClazz, "getByteArray", "(I)[[B"), (jint)classCount);//jniEnv->NewObjectArray(classCount, jniEnv->FindClass("[B"), NULL);
            int cptr = 0;
            for (jsize j = 0; j < classCount; j++)
            {
                jbyteArray classByteArray = jniEnv->NewByteArray(classSizes[j]);
                jniEnv->SetByteArrayRegion(classByteArray, 0, classSizes[j], (jbyte *)(classes + cptr));
                cptr += classSizes[j];
                jniEnv->SetObjectArrayElement(classesData, j, classByteArray);
            }
            jint injectResult = jniEnv->CallStaticIntMethod(classLoaderClazz, jniEnv->GetStaticMethodID(classLoaderClazz, "injectCP", "([[B)I"), classesData);
            if (injectResult)
            {
                MessageBoxA(NULL, "Error on injecting: injectResult != 0", "ELoader", MB_OK | MB_ICONERROR);
                jvm->DetachCurrentThread();
                break;
            }
            MessageBoxA(NULL, "Injected successfully!", "ELoader", MB_OK | MB_ICONINFORMATION);
            jvm->DetachCurrentThread();
        }
    }
    ExitThread(0);
}

extern "C" DLL_EXPORT BOOL APIENTRY DllMain(HINSTANCE hinstDLL, DWORD fdwReason, LPVOID lpvReserved)
{
    switch (fdwReason)
    {
        case DLL_PROCESS_ATTACH:
            CreateThread(NULL, 0, &MainThread, NULL, 0, NULL);
            break;
    }
    return TRUE; // succesful
}
