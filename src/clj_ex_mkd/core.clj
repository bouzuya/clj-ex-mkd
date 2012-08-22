(ns clj-ex-mkd.core
  (:require [clojure.java.io :as io]
            [net.cgrand.enlive-html :as eh])
  (:import (com.petebevin.markdown MarkdownProcessor)))

(defn markdown-to-html
  [markdown]
  (->
    (MarkdownProcessor.)
    (.markdown markdown)))

(defn load-jekyll-post-file
  [post-file]
  (let [text (slurp post-file)
        [_ yaml content] (re-find #"(?m)(?s)^---\s*(.*?)^---\s*^(.*)" text)]
    (reduce (fn [m [_ k v]] (assoc m (keyword k) v))
            {:content (markdown-to-html content)}
            (re-seq #"(\w+):\s*(.*)\n" yaml))))

(def LAYOUT_FILE "./template/layouts/post.html")
(def JEKYLL_POST_FILE "./template/posts/2012-02-16-jekyll.markdown")

(eh/deftemplate post-html (io/file LAYOUT_FILE)
  [post]
  [:title] (eh/content (:title post))
  [:article :header :h1] (eh/content (:title post))
  [:article :div.post-body] (eh/html-content (:content post)))

(defn -main
  [& args]
  (let [data (load-jekyll-post-file JEKYLL_POST_FILE)
        compiler #(apply str (post-html %))]
    (print (compiler data))))

