from pathlib import Path
import subprocess


FONT = {
    "A": ["01110", "10001", "10001", "11111", "10001", "10001", "10001"],
    "B": ["11110", "10001", "10001", "11110", "10001", "10001", "11110"],
    "C": ["01111", "10000", "10000", "10000", "10000", "10000", "01111"],
    "D": ["11110", "10001", "10001", "10001", "10001", "10001", "11110"],
    "E": ["11111", "10000", "10000", "11110", "10000", "10000", "11111"],
    "F": ["11111", "10000", "10000", "11110", "10000", "10000", "10000"],
    "G": ["01111", "10000", "10000", "10111", "10001", "10001", "01111"],
    "H": ["10001", "10001", "10001", "11111", "10001", "10001", "10001"],
    "I": ["11111", "00100", "00100", "00100", "00100", "00100", "11111"],
    "J": ["00111", "00010", "00010", "00010", "10010", "10010", "01100"],
    "K": ["10001", "10010", "10100", "11000", "10100", "10010", "10001"],
    "L": ["10000", "10000", "10000", "10000", "10000", "10000", "11111"],
    "M": ["10001", "11011", "10101", "10101", "10001", "10001", "10001"],
    "N": ["10001", "11001", "10101", "10011", "10001", "10001", "10001"],
    "O": ["01110", "10001", "10001", "10001", "10001", "10001", "01110"],
    "P": ["11110", "10001", "10001", "11110", "10000", "10000", "10000"],
    "Q": ["01110", "10001", "10001", "10001", "10101", "10010", "01101"],
    "R": ["11110", "10001", "10001", "11110", "10100", "10010", "10001"],
    "S": ["01111", "10000", "10000", "01110", "00001", "00001", "11110"],
    "T": ["11111", "00100", "00100", "00100", "00100", "00100", "00100"],
    "U": ["10001", "10001", "10001", "10001", "10001", "10001", "01110"],
    "V": ["10001", "10001", "10001", "10001", "10001", "01010", "00100"],
    "W": ["10001", "10001", "10001", "10101", "10101", "10101", "01010"],
    "X": ["10001", "10001", "01010", "00100", "01010", "10001", "10001"],
    "Y": ["10001", "10001", "01010", "00100", "00100", "00100", "00100"],
    "Z": ["11111", "00001", "00010", "00100", "01000", "10000", "11111"],
    "0": ["01110", "10001", "10011", "10101", "11001", "10001", "01110"],
    "1": ["00100", "01100", "00100", "00100", "00100", "00100", "01110"],
    "2": ["01110", "10001", "00001", "00010", "00100", "01000", "11111"],
    "3": ["11110", "00001", "00001", "01110", "00001", "00001", "11110"],
    "4": ["00010", "00110", "01010", "10010", "11111", "00010", "00010"],
    "5": ["11111", "10000", "10000", "11110", "00001", "00001", "11110"],
    "6": ["01110", "10000", "10000", "11110", "10001", "10001", "01110"],
    "7": ["11111", "00001", "00010", "00100", "01000", "01000", "01000"],
    "8": ["01110", "10001", "10001", "01110", "10001", "10001", "01110"],
    "9": ["01110", "10001", "10001", "01111", "00001", "00001", "01110"],
    " ": ["00000", "00000", "00000", "00000", "00000", "00000", "00000"],
}


WHITE = (255, 255, 255)
BLACK = (26, 32, 44)
BLUE = (212, 232, 255)
GREEN = (224, 244, 225)
GOLD = (255, 244, 214)
ROSE = (255, 227, 232)
GRAY = (239, 242, 247)


def canvas(width, height, color=WHITE):
    return [[color for _ in range(width)] for _ in range(height)]


def set_px(img, x, y, color):
    if 0 <= y < len(img) and 0 <= x < len(img[0]):
        img[y][x] = color


def fill_rect(img, x, y, w, h, fill, border=BLACK):
    for yy in range(y, y + h):
        for xx in range(x, x + w):
            set_px(img, xx, yy, fill)
    for xx in range(x, x + w):
        set_px(img, xx, y, border)
        set_px(img, xx, y + h - 1, border)
    for yy in range(y, y + h):
        set_px(img, x, yy, border)
        set_px(img, x + w - 1, yy, border)


def line(img, x1, y1, x2, y2, color=BLACK):
    dx = abs(x2 - x1)
    dy = -abs(y2 - y1)
    sx = 1 if x1 < x2 else -1
    sy = 1 if y1 < y2 else -1
    err = dx + dy
    while True:
        set_px(img, x1, y1, color)
        if x1 == x2 and y1 == y2:
            break
        e2 = 2 * err
        if e2 >= dy:
            err += dy
            x1 += sx
        if e2 <= dx:
            err += dx
            y1 += sy


def arrow(img, x1, y1, x2, y2, color=BLACK):
    line(img, x1, y1, x2, y2, color)
    if abs(x2 - x1) >= abs(y2 - y1):
        direction = 1 if x2 >= x1 else -1
        line(img, x2, y2, x2 - 12 * direction, y2 - 6, color)
        line(img, x2, y2, x2 - 12 * direction, y2 + 6, color)
    else:
        direction = 1 if y2 >= y1 else -1
        line(img, x2, y2, x2 - 6, y2 - 12 * direction, color)
        line(img, x2, y2, x2 + 6, y2 - 12 * direction, color)


def draw_char(img, x, y, ch, scale=3, color=BLACK):
    glyph = FONT.get(ch.upper(), FONT[" "])
    for row, bits in enumerate(glyph):
        for col, bit in enumerate(bits):
            if bit == "1":
                for yy in range(scale):
                    for xx in range(scale):
                        set_px(img, x + col * scale + xx, y + row * scale + yy, color)


def draw_text_centered(img, x, y, lines, scale=3, color=BLACK):
    char_w = 5 * scale
    spacing = scale
    line_h = 7 * scale + scale * 2
    total_h = len(lines) * line_h - scale * 2
    current_y = y - total_h // 2
    for text in lines:
        width = len(text) * (char_w + spacing) - spacing
        current_x = x - width // 2
        for ch in text:
            draw_char(img, current_x, current_y, ch, scale, color)
            current_x += char_w + spacing
        current_y += line_h


def draw_box(img, x, y, w, h, title_lines, fill):
    fill_rect(img, x, y, w, h, fill)
    draw_text_centered(img, x + w // 2, y + h // 2, title_lines)


def save_ppm(img, path):
    height = len(img)
    width = len(img[0])
    with open(path, "w", encoding="ascii") as f:
        f.write(f"P3\n{width} {height}\n255\n")
        for row in img:
            f.write(" ".join(f"{r} {g} {b}" for r, g, b in row))
            f.write("\n")


def ppm_to_png(ppm_path, png_path):
    subprocess.run(
        ["sips", "-s", "format", "png", str(ppm_path), "--out", str(png_path)],
        check=True,
        stdout=subprocess.DEVNULL,
        stderr=subprocess.DEVNULL,
    )


def context_diagram(out_dir):
    img = canvas(1400, 900)
    draw_box(img, 550, 300, 300, 180, ["DIGITAL THERAPY", "ASSISTANT"], BLUE)
    draw_box(img, 120, 120, 230, 120, ["PATIENT"], GREEN)
    draw_box(img, 120, 390, 230, 120, ["THERAPIST"], GREEN)
    draw_box(img, 120, 660, 230, 120, ["ADMINISTRATOR"], GREEN)
    draw_box(img, 1040, 120, 230, 120, ["LLM PROVIDER"], GOLD)
    draw_box(img, 1040, 390, 230, 120, ["EMAIL SERVICE"], GOLD)
    draw_box(img, 1040, 660, 230, 120, ["EHR SYSTEMS"], GOLD)
    arrow(img, 350, 180, 550, 350)
    arrow(img, 350, 450, 550, 390)
    arrow(img, 350, 720, 550, 430)
    arrow(img, 850, 350, 1040, 180)
    arrow(img, 850, 390, 1040, 450)
    arrow(img, 850, 430, 1040, 720)
    save_diagram(img, out_dir / "c4-context")


def container_diagram(out_dir):
    img = canvas(1400, 900)
    draw_box(img, 90, 320, 220, 120, ["USER"], GREEN)
    draw_box(img, 380, 120, 280, 150, ["CLI", "APPLICATION"], BLUE)
    draw_box(img, 380, 370, 280, 150, ["SPRING BOOT", "APPLICATION"], BLUE)
    draw_box(img, 750, 120, 250, 150, ["H2", "DATABASE"], GOLD)
    draw_box(img, 750, 370, 250, 150, ["SIMPLE", "VECTOR STORE"], GOLD)
    draw_box(img, 1080, 240, 250, 150, ["KNOWLEDGE", "BASE"], GOLD)
    arrow(img, 310, 380, 380, 195)
    arrow(img, 310, 380, 380, 445)
    arrow(img, 660, 445, 750, 195)
    arrow(img, 660, 445, 750, 445)
    arrow(img, 1000, 445, 1080, 315)
    arrow(img, 1000, 195, 1080, 315)
    save_diagram(img, out_dir / "c4-container")


def component_diagram(out_dir):
    img = canvas(1600, 1000)
    draw_box(img, 80, 80, 300, 260, ["CONTROLLERS"], BLUE)
    draw_text_centered(img, 230, 230, ["AUTH", "SESSION", "DIARY", "PROGRESS", "CRISIS"])
    draw_box(img, 470, 80, 300, 260, ["SERVICES"], GREEN)
    draw_text_centered(img, 620, 230, ["AUTH", "SESSION", "DIARY", "PROGRESS", "CRISIS", "AI"])
    draw_box(img, 860, 80, 300, 260, ["SECURITY"], ROSE)
    draw_text_centered(img, 1010, 230, ["SECURITYCONFIG", "JWTAUTHFILTER", "JWTUTIL", "USERDETAILS"])
    draw_box(img, 1250, 80, 260, 260, ["AI", "INTEGRATION"], GOLD)
    draw_text_centered(img, 1380, 230, ["KNOWLEDGEBASE", "RAGCONTEXT", "CRISISDETECTOR"])
    draw_box(img, 470, 500, 500, 260, ["REPOSITORIES"], GRAY)
    draw_text_centered(img, 720, 640, ["USER", "USERSESSION", "DIARYENTRY", "REFRESHTOKEN", "TRUSTEDCONTACT"])
    arrow(img, 380, 210, 470, 210)
    arrow(img, 770, 210, 860, 210)
    arrow(img, 770, 240, 1250, 240)
    arrow(img, 620, 340, 620, 500)
    save_diagram(img, out_dir / "c4-component")


def code_diagram(out_dir):
    img = canvas(1600, 1000)
    draw_box(img, 80, 80, 360, 120, ["DIGITALTHERAPY", "ASSISTANTAPPLICATION"], BLUE)
    draw_box(img, 80, 280, 360, 120, ["DTACLI", "MENUHANDLER", "COMMAND"], GREEN)
    draw_box(img, 560, 80, 360, 120, ["AUTHSERVICEIMPL", "SESSIONSERVICEIMPL", "DIARYSERVICEIMPL"], GOLD)
    draw_box(img, 560, 280, 360, 120, ["PROGRESSSERVICEIMPL", "CRISISSERVICEIMPL", "AISERVICEIMPL"], GOLD)
    draw_box(img, 1060, 80, 360, 120, ["JWTUTIL", "USER", "USERSESSION"], ROSE)
    draw_box(img, 1060, 280, 360, 120, ["DIARYENTRY", "REFRESHTOKEN"], ROSE)
    arrow(img, 260, 200, 260, 280)
    arrow(img, 440, 140, 560, 140)
    arrow(img, 440, 340, 560, 340)
    arrow(img, 920, 140, 1060, 140)
    arrow(img, 920, 340, 1060, 340)
    arrow(img, 740, 200, 740, 280)
    save_diagram(img, out_dir / "c4-code")


def save_diagram(img, base_path):
    ppm_path = base_path.with_suffix(".ppm")
    png_path = base_path.with_suffix(".png")
    save_ppm(img, ppm_path)
    ppm_to_png(ppm_path, png_path)
    ppm_path.unlink(missing_ok=True)


def main():
    out_dir = Path("docs/diagrams/c4")
    out_dir.mkdir(parents=True, exist_ok=True)
    context_diagram(out_dir)
    container_diagram(out_dir)
    component_diagram(out_dir)
    code_diagram(out_dir)


if __name__ == "__main__":
    main()
