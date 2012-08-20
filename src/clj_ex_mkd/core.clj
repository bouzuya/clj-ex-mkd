(ns clj-ex-mkd.core
  (:import (com.petebevin.markdown MarkdownProcessor)))

(defn markdown-to-html
  [markdown]
  (->
    (MarkdownProcessor.)
    (.markdown markdown)))

(defn read-markdown-file
  [file-name]
  (with-open [reader (clojure.java.io/reader file-name)]
    (markdown-to-html (apply str (interpose "\n" (line-seq reader))))))

(defn -main
  [& args]
  (print (read-markdown-file "./README.md")))

