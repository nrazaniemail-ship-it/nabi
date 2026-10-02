## Namello 1.20.0 — Security & Google Account
- Google Sign-In (نیازمند OAuth Web Client ID)
- Secure backup/restore با AES-256-GCM + PBKDF2-SHA256
- Google Drive App Data برای بکاپ خصوصی و رمزگذاری‌شده
- رمز Google یا access token در storage برنامه ذخیره نمی‌شود

# Namello 1.19.0 Android + Native Widget

Version Code: 30

Includes the Namello PWA engine plus native Android App Widget, notifications, MT5 bridge support, Trade/Day Plans, Checklist and Rule Compliance analysis.

## Namello 1.21 Account & Sync
این نسخه Sync چنددستگاهی را با envelope رمزگذاری‌شده و revision/conflict handling آماده می‌کند. برای احراز هویت واقعی چنددستگاهی، backend باید Google token را سمت سرور verify کند؛ قرارداد پیشنهادی در `SYNC_BACKEND_SPEC.md` است.

### Namello 1.22 Backend
این پروژه حالا یک Backend واقعی در پوشه `backend/` دارد. برای حساب و Sync چنددستگاهی، Backend را با Node.js 22+ اجرا کن و `GOOGLE_CLIENT_ID` و `NAMELLO_SESSION_SECRET` را تنظیم کن. آدرس Backend را در تنظیمات Namello وارد کن.
