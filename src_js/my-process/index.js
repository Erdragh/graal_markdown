// The vfile package uses the cwd as a placeholder
// for the directory stored in the virtual file.
// Since that is not needed for the functionality
// of the markdown parsing process, I just return
// the root directory.
module.exports = {
  cwd() {
    return "/";
  }
}