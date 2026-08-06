const fs = require('fs');
const path = require('path');

const src = 'C:\\Users\\Master\\.gemini\\antigravity-ide\\brain\\f3e50cc5-2eec-4314-946d-1aa90d752546\\hero_isometric_3d_1785818291577.png';
const destDir = 'd:\\JDBCSW\\MailAlly\\mailally-backend\\mailally-frontend\\public';
const dest = path.join(destDir, 'hero_isometric_3d.png');

if (!fs.existsSync(destDir)) {
  fs.mkdirSync(destDir, { recursive: true });
}

fs.copyFileSync(src, dest);
console.log('Successfully copied to:', dest);
