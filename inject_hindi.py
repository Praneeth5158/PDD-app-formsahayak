import glob, re

translations = {
    "1.0x": "1.0x",
    "3 Errors Found": "3 त्रुटियाँ मिलीं",
    "Account Number": "खाता संख्या",
    "Address": "पता",
    "All Permissions Granted Successfully": "सभी अनुमतियाँ सफलतापूर्वक दी गईं",
    "All Permissions Set!": "सभी अनुमतियाँ सेट हो गईं!",
    "All permissions are required for the app to work properly": "ऐप को ठीक से काम करने के लिए सभी अनुमतियों की आवश्यकता है",
    "All permissions are required to continue": "जारी रखने के लिए सभी अनुमतियों की आवश्यकता है",
    "App highlights important fields you need to fill": "ऐप उन महत्वपूर्ण फ़ील्ड को हाइलाइट करता है जिन्हें आपको भरना है",
    "Avoid shadows on the form": "फॉर्म पर परछाई से बचें",
    "Back to Home": "होम पर वापस जाएं",
    "Back to Login": "लॉगिन पर वापस जाएं",
    "Bank Type": "बैंक का प्रकार",
    "Camera": "कैमरा",
    "Camera Access": "कैमरा एक्सेस",
    "Can we proceed with this form?": "क्या हम इस फॉर्म के साथ आगे बढ़ सकते हैं?",
    "Can't see pop-up? Open Settings": "पॉप-अप नहीं दिख रहा? सेटिंग्स खोलें",
    "Cancel": "रद्द करें",
    "Change": "बदलें",
    "Change Password": "पासवर्ड बदलें",
    "Change Photo": "फोटो बदलें",
    "Code sent to: ": "कोड भेजा गया: ",
    "Confidence": "विश्वास (Confidence)",
    "Confirm Password": "पासवर्ड की पुष्टि करें",
    "Confirm new password": "नए पासवर्ड की पुष्टि करें",
    "Continue": "जारी रखें",
    "Continue to Dashboard": "डैशबोर्ड पर जारी रखें",
    "Create a new password for your account": "अपने खाते के लिए नया पासवर्ड बनाएँ",
    "Detected Form Type:": "पहचाना गया फॉर्म प्रकार:",
    "Didn't receive code? Resend OTP": "कोड नहीं मिला? OTP फिर से भेजें",
    "Document Pages": "दस्तावेज़ पृष्ठ",
    "Don't have an account? ": "क्या आपके पास खाता नहीं है? ",
    "Edit Profile": "प्रोफ़ाइल संपादित करें",
    "Email": "ईमेल",
    "Ensure all fields are visible": "सुनिश्चित करें कि सभी फ़ील्ड दिखाई दे रहे हैं",
    "Enter new password": "नया पासवर्ड दर्ज करें",
    "Errors Found": "त्रुटियाँ मिलीं",
    "Finish": "समाप्त करें",
    "Fix Errors": "त्रुटियाँ सुधारें",
    "Forgot Password?": "पासवर्ड भूल गए?",
    "Form Guide": "फॉर्म गाइड",
    "FormSahayak needs these permissions to help you fill forms": "फॉर्म भरने में आपकी मदद करने के लिए FormSahayak को इन अनुमतियों की आवश्यकता है",
    "Forms": "फॉर्म्स",
    "Forms History": "फॉर्म्स इतिहास",
    "Full Name": "पूरा नाम",
    "Gallery": "गैलरी",
    "Get Started": "शुरू करें",
    "Get Started >": "शुरू करें >",
    "Get voice instructions in your regional language": "अपनी क्षेत्रीय भाषा में ध्वनि निर्देश प्राप्त करें",
    "Go Back": "वापस जाएँ",
    "Grant All Permissions": "सभी अनुमतियाँ दें",
    "Grant Permission": "अनुमति दें",
    "Grant Permissions": "अनुमतियाँ दें",
    "Guide": "गाइड",
    "Help": "सहायता",
    "Highlighted Fields": "हाइलाइट किए गए फ़ील्ड",
    "Hold your phone steady": "अपने फ़ोन को स्थिर रखें",
    "If the form type is correct, click 'Proceed' to continue. Otherwise, click 'Re-scan Form' to upload again.": "यदि फॉर्म का प्रकार सही है, तो जारी रखने के लिए 'आगे बढ़ें' पर क्लिक करें। अन्यथा, फिर से अपलोड करने के लिए 'फॉर्म फिर से स्कैन करें' पर क्लिक करें।",
    "Image is Unclear": "छवि अस्पष्ट है",
    "Language": "भाषा",
    "Language Preference": "भाषा की प्राथमिकता",
    "Listen to Form Type": "फॉर्म प्रकार सुनें",
    "Login": "लॉगिन",
    "Login to continue": "जारी रखने के लिए लॉगिन करें",
    "Logout": "लॉग आउट",
    "Make sure the form is well-lit": "सुनिश्चित करें कि फॉर्म पर पर्याप्त रोशनी है",
    "Microphone": "माइक्रोफ़ोन",
    "Microphone Access": "माइक्रोफ़ोन एक्सेस",
    "My Forms": "मेरे फॉर्म्स",
    "NEED HELP?": "क्या सहायता चाहिए?",
    "Navigate Fields": "फ़ील्ड नेविगेट करें",
    "New Password": "नया पासवर्ड",
    "Next": "अगला",
    "Next >": "अगला >",
    "No, Re-scan Form": "नहीं, फॉर्म फिर से स्कैन करें",
    "Pages": "पृष्ठ",
    "Password": "पासवर्ड",
    r"Password Reset\nSuccessfully!": r"पासवर्ड सफलतापूर्वक\nरीसेट हो गया!",
    "Permission Granted ✅": "अनुमति दी गई ✅",
    "Personal Information": "व्यक्तिगत जानकारी",
    "Phone Number": "फ़ोन नंबर",
    "Please check and fix them": "कृपया जांचें और उन्हें सुधारें",
    "Please wait...": "कृपया प्रतीक्षा करें...",
    "Premium Member": "प्रीमियम सदस्य",
    "Previous": "पिछला",
    "Privacy First: ": "प्राइवेसी प्रथम: ",
    "Profile": "प्रोफ़ाइल",
    "Recommended: Normal speed provides a good balance between clarity and efficiency": "सुझाया गया: सामान्य गति स्पष्टता और दक्षता के बीच अच्छा संतुलन प्रदान करती है",
    r"Registered\nSuccessfully! 🎉": r"सफलतापूर्वक\nपंजीकृत! 🎉",
    "Required for voice guidance": "ध्वनि मार्गदर्शन के लिए आवश्यक",
    "Required to save documents": "दस्तावेज़ सहेजने के लिए आवश्यक",
    "Required to scan documents": "दस्तावेज़ स्कैन करने के लिए आवश्यक",
    "Save Changes": "परिवर्तन सहेजें",
    "Scan Bank Forms": "बैंक फॉर्म स्कैन करें",
    "Search forms...": "फॉर्म खोजें...",
    "Select Language": "भाषा चुनें",
    "Settings": "सेटिंग्स",
    "Sign Up": "साइन अप",
    "Signature": "हस्ताक्षर",
    "Skip": "छोड़ें",
    "Smart Form Guidance for Everyone": "सभी के लिए स्मार्ट फॉर्म मार्गदर्शन",
    "Standard speaking speed for most users": "अधिकांश उपयोगकर्ताओं के लिए मानक बोलने की गति",
    "Status": "स्थिति",
    "Storage": "स्टोरेज",
    "Storage Access": "स्टोरेज एक्सेस",
    r"Take a clear photo or upload\nfrom gallery": r"स्पष्ट फोटो लें या गैलरी\nसे अपलोड करें",
    "Tap to get started": "शुरू करने के लिए टैप करें",
    "The uploaded image is blurry or unclear. Please upload a clear photo of the form.": "अपलोड की गई छवि धुंधली या अस्पष्ट है। कृपया फॉर्म की स्पष्ट फोटो अपलोड करें।",
    "Tips for best results": "बेहतर परिणामों के लिए टिप्स",
    "Tips for better images:": "बेहतर छवियों के लिए टिप्स:",
    "Try Again": "पुनः प्रयास करें",
    "Unclear": "अस्पष्ट",
    "Upload Form": "फॉर्म अपलोड करें",
    "Upload a Form": "फॉर्म अपलोड करें",
    "Use your phone camera to scan bank forms easily": "बैंक फॉर्म को आसानी से स्कैन करने के लिए अपने फोन कैमरे का उपयोग करें",
    "Verify OTP": "OTP सत्यापित करें",
    "Verifying Form": "फॉर्म सत्यापित हो रहा है",
    "Voice Guidance": "ध्वनि मार्गदर्शन",
    "We identified the type of form you uploaded": "हमने आपके द्वारा अपलोड किए गए फॉर्म के प्रकार की पहचान की है",
    "Welcome Back": "वापसी पर स्वागत है",
    "Welcome back!": "वापसी पर स्वागत है!",
    "Yes, Proceed with this Form": "हां, इस फॉर्म के साथ आगे बढ़ें",
    r"You can now login with your new\npassword": r"अब आप अपने नए\nपासवर्ड के साथ लॉगिन कर सकते हैं",
    "Your account has been created": "आपका खाता बन गया है",
    "Your data stays on your device. We only use these permissions to help you.": "आपका डेटा आपके डिवाइस पर रहता है। हम केवल आपकी मदद के लिए इन अनुमतियों का उपयोग करते हैं।",
    "Your password has been changed": "आपका पासवर्ड बदल दिया गया है",
    "தெளிவற்றது": "अस्पष्ट",
    "అస్పష్టంగా ఉంది": "अस्पष्ट",
    "🙏 Welcome to FormSahayak": "🙏 FormSahayak में आपका स्वागत है"
}

for f in glob.glob('app/src/main/java/com/simats/formsahayak/ui/screens/*.kt'):
    with open(f, 'r', encoding='utf-8') as file:
        content = file.read()
    
    def repl(m):
        ta_line = m.group(1)
        else_line = m.group(2)
        english_str = m.group(3)
        hindi_str = translations.get(english_str, english_str)
        return f'{ta_line}\n        "hi" -> "{hindi_str}"\n        {else_line}'

    # match "ta" -> "..." \n else -> "..."
    new_content = re.sub(
        r'(\"ta\"\s*->\s*\".*?\")\s*(else\s*->\s*\"(.*?)\")',
        repl,
        content
    )
    
    # Special cases for ResetPasswordScreen (isChangePassword)
    def repl_special(m):
        ta_line = m.group(1)
        else_line = m.group(2)
        eng_true = m.group(3)
        eng_false = m.group(4)
        hi_true = translations.get(eng_true, eng_true)
        hi_false = translations.get(eng_false, eng_false)
        return f'{ta_line}\n        "hi" -> if (isChangePassword) "{hi_true}" else "{hi_false}"\n        {else_line}'
    
    new_content = re.sub(
        r'(\"ta\"\s*->\s*if\s*\(isChangePassword\)\s*\".*?\"\s*else\s*\".*?\")\s*(else\s*->\s*if\s*\(isChangePassword\)\s*\"(.*?)\"\s*else\s*\"(.*?)\")',
        repl_special,
        new_content
    )
    
    # Special case for 0.75x 1.25x (VoiceGuidanceSettingsScreen)
    # text = when(speed) { "Slow" -> "0.75x"; "Fast" -> "1.25x"; else -> "1.0x" } doesn't match our pattern because it's when(speed), not selectedLanguage
    
    with open(f, 'w', encoding='utf-8') as file:
        file.write(new_content)

