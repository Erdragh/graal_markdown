import rehypeFormat from "rehype-format";
import rehypeSanitize from "rehype-sanitize";
import rehypeStringify from "rehype-stringify";
import remarkParse from "remark-parse";
import remarkRehype from "remark-rehype";
import remarkToc from "remark-toc";
import { unified } from "unified";

export default function parseMarkdown(input) {
  // currently doesn't use any custom plugins, but it could.
  const result = unified()
    .use(remarkParse)
    .use(remarkToc)
    .use(remarkRehype, {
      allowDangerousHtml: true
    })
    // .use(rehypeSanitize)
    .use(rehypeFormat)
    .use(rehypeStringify, {
      allowDangerousHtml: true
    })
    .processSync(input);
  return String(result);
}