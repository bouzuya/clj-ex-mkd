(ns clj-ex-mkd.core
  (:import (com.petebevin.markdown MarkdownProcessor)))

(defn markdown-to-html
  [markdown]
  (->
    (MarkdownProcessor.)
    (.markdown markdown)))

(defn load-markdown-file
  [markdown-file]
  (markdown-to-html (slurp markdown-file)))

(defn load-jekyll-post-file
  [post-file]
  (let [text (slurp post-file)
        [_ yaml content] (re-find #"(?m)(?s)^---\s*(.*?)^---\s*^(.*)" text)]
    (reduce (fn [m [_ k v]] (assoc m (keyword k) v))
            {:content (markdown-to-html content)}
            (re-seq #"(\w+):\s*(.*)\n" yaml))))

(defn -main
  [& args]
  (print (load-markdown-file "./README.md"))
  (print (load-jekyll-post-file "./2012-02-16-jekyll.markdown")))


