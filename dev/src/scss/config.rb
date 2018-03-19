# Get the directory that this configuration file exists in
dir = File.dirname(__FILE__)

# Load the file automatically
#load File.join(dir, "..", "themes")

#Compass configuration
sass_path = dir
css_path = File.join(dir, "..", "css")
environment = :development # :production
output_style = :expanded # :compressed
