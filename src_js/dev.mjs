import parseMarkdown from "./start.mjs";
import {readFileSync} from "fs";

const read = readFileSync("../test.md");

console.log(parseMarkdown(read))