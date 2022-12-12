from os import system
from sys import argv

system(f"konsole -e \"bash -c \\\"{' '.join(argv[1:3])};read -p \\\"Press_enter_to_close...\\\"\\\"\"")
