// Somewhere in the parsing process this specific method of the
// node/browser URL api is needed, but I couldn't find a working
// polyfill providing it. Since we won't be using file urls, just
// returning the string again shouldn't be an issue.
module.exports = {
  fileURLToPath(fileUrl) {
    return fileUrl;
  }
}