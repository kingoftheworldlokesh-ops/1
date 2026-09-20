# 🚫 YouTube Shorts Limiter

An Android application leveraging the **Android Accessibility Service API** designed to curb doom-scrolling by enforcing a strict **one-and-done** policy on YouTube Shorts. 

## 🚀 How It Works
1. **Detection:** The background utility listens exclusively for window layout updates coming from `com.google.android.youtube`.
2. **First Short Exemption:** The initial short video you load runs completely unaffected.
3. **Automated Intervention:** If the app notices a structural layout disruption (indicating you have swiped/scrolled to a second video), it triggers a target macro intent that immediately drops you back out to the **Android Home Launcher Screen**.

## 🛠️ Local Build Setup
Make sure you have **Android Studio Jellyfish** (or newer) and **JDK 17** properly installed.

```bash
# Clone the repository
git clone https://github.com

# Navigate into the project directory
cd shorts-blocker

# Build the Debug package package
./gradlew assembleDebug
```

## 🤖 Continuous Integration
Every branch `push` or incoming `pull request` triggers a automated compilation pipeline inside **GitHub Actions**. 

To download your fresh compiled binaries:
1. Navigate over to the **Actions** tab inside this GitHub repository.
2. Select the latest active run pipeline workflow.
3. Scroll right down to the **Artifacts** display window block to fetch the standalone `.apk` build zip archive.

## ⚠️ Accessibility Warning
Because this utility intercepts live window updates to enforce system-wide screen constraints, you **must manually authorize Accessibility Permissions** under `Settings -> Accessibility -> Installed Apps -> Shorts Limiter` on your physical device for the background loop execution logic to begin working.
