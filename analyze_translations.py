import glob
import re

pattern = r'when\s*\(\s*selectedLanguage\??\.code\s*\)\s*\{(.*?)\}'

all_when_blocks = []
total_count = 0

for filepath in glob.glob('app/src/main/java/com/simats/formsahayak/**/*.kt', recursive=True):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    matches = re.finditer(pattern, content, re.DOTALL)
    for m in matches:
        total_count += 1
        block_content = m.group(1).strip()
        all_when_blocks.append((filepath, block_content))

with open('when_blocks_analysis.txt', 'w', encoding='utf-8') as out:
    out.write(f"Total when blocks found: {total_count}\n")
    for path, block in all_when_blocks:
        out.write(f"\n--- File: {path} ---\n")
        out.write(block + "\n")
print(f"Analysis completed. Saved {total_count} when blocks to when_blocks_analysis.txt")
