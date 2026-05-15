import re
import sys

def replace_in_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Replacements mapping
    replacements = {
        r'\b2\.dp\b': 'Dimens.SpacingExtraSmall',
        r'\b4\.dp\b': 'Dimens.SpacingSmall',
        r'\b6\.dp\b': 'Dimens.SpacingMediumSmall',
        r'\b8\.dp\b': 'Dimens.SpacingMedium',
        r'\b12\.dp\b': 'Dimens.SpacingMediumLarge',
        r'\b16\.dp\b': 'Dimens.SpacingLarge',
        r'\b24\.dp\b': 'Dimens.SpacingExtraLarge',
        r'\b32\.dp\b': 'Dimens.SpacingExtraExtraLarge',
        r'\b48\.dp\b': 'Dimens.SpacingHuge',
        r'\b56\.dp\b': 'Dimens.SpacingMassive',
        r'\b10\.dp\b': 'Dimens.IconSizeExtraSmall'
    }

    # Add imports if necessary
    import_statement = "import dev.therealashik.github.Dimens\n"
    if "import dev.therealashik.github.Dimens" not in content and any(re.search(pattern, content) for pattern in replacements):
        # find the last import
        last_import = content.rfind("import ")
        if last_import != -1:
            end_of_line = content.find("\n", last_import)
            content = content[:end_of_line+1] + import_statement + content[end_of_line+1:]

    for pattern, replacement in replacements.items():
        content = re.sub(pattern, replacement, content)

    # Additional specific replacements for Icons where variable names might conflict with Spacing vs IconSize
    content = re.sub(r'size\(Dimens\.SpacingExtraLarge\)', 'size(Dimens.IconSizeMedium)', content)
    content = re.sub(r'size\(Dimens\.SpacingHuge\)', 'size(Dimens.IconSizeLarge)', content)

    with open(filepath, 'w') as f:
        f.write(content)

if __name__ == '__main__':
    replace_in_file(sys.argv[1])
