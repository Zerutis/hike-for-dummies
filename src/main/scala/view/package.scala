package dummies

import model.Hike

import zio.http.html.{Dom, _}

package object View {
  private val pageConfig: Html = html(
    langAttr := "en",
    head(
      meta(charsetAttr := "utf-8"),
      meta(nameAttr := "viewport", contentAttr := "width=device-width, initial-scale=1, shrink-to-fit=no"),
      meta(nameAttr := "description", contentAttr := ""),
      meta(nameAttr := "author", contentAttr := ""),
      link(relAttr := "icon", hrefAttr := "favicon.ico"),
      title("Hikes for dummies"),
      link(
        relAttr := "stylesheet",
        hrefAttr := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
      ),
      script(srcAttr := "https://code.jquery.com/jquery-3.5.1.slim.min.js"),
      script(srcAttr := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"),
    ),
  )

  def index(hikes: List[Hike]): Html = pageConfig ++ Html.fromDomElement(
    Dom.element(
      "body",
      Dom.element(
        "h1",
        Dom.attr("class", "text-center"),
        Dom.text("Hikes for dummies using MVC")
        ),
      Dom.element(
        "table",
        Dom.attr("class", "table table-striped table-bordered table-hover"),
        Dom.element(
          "thead",
          Dom.element(
            "tr",
            Dom.element("th", Dom.text("Name")),
            Dom.element("th", Dom.text("Distance")),
            Dom.element("th", Dom.text("Elevation")),
            Dom.element("th", Dom.text("Difficulty")),
            Dom.element("th", Dom.text("Description")),
            Dom.element("th", Dom.text("Actions")),
            )
          ),
        Dom.element(
          "tbody",
          hikes.map(
            hike =>
              Dom.element(
                "tr",
                Dom.element("td", Dom.text(hike.name)),
                Dom.element("td", Dom.text(hike.distance.toString)),
                Dom.element("td", Dom.text(hike.elevation.toString)),
                Dom.element("td", Dom.text(hike.difficulty)),
                Dom.element("td", Dom.text(hike.description)),
                Dom.element(
                  "td",
                  Dom.element(
                    "button",
                    Dom.text("Edit"),
                    Dom.attr("type", "submit"),
                    Dom.attr("class", "btn btn-success"),
                    ),
                  Dom.element(
                    "a",
                    Dom.text("Delete"),
                    Dom.attr("role", "button"),
                    Dom.attr("class", "btn btn-danger"),
                    Dom.attr("href", s"/mvc/hikes/${hike.id}"),
                  )
                ),
              )
            ): _*
          )
        ),
      Dom.element(
        "div",
        Dom.attr("class", "container p-10 my-10 border border-primary rounded"),
        Dom.attr("style", "width: 50%; margin: 0 auto; margin-top: 20px; margin-bottom: 20px; padding: 20px;"),
        Dom.element("h2", Dom.text("Add a new hike")),
        Dom.element(
          "form",
          Dom.attr("action", "/mvc/hikes"),
          Dom.attr("method", "post"),
          Dom.attr("class", "form-horizontal"),
          Dom.attr("enctype", "text/plain"),
          Dom.element(
            "div",
            Dom.attr("class", "form-group"),
            Dom.element(
              "label",
              Dom.attr("for", "name"),
              Dom.text("Name"),
              ),
            Dom.element(
              "input",
              Dom.attr("type", "text"),
              Dom.attr("class", "form-control"),
              Dom.attr("id", "name"),
              Dom.attr("name", "name"),
              ),
            ),
          Dom.element(
            "div",
            Dom.attr("class", "form-group"),
            Dom.element(
              "label",
              Dom.attr("for", "distance"),
              Dom.text("Distance"),
              ),
            Dom.element(
              "input",
              Dom.attr("type", "number"),
              Dom.attr("class", "form-control"),
              Dom.attr("id", "distance"),
              Dom.attr("name", "distance"),
              ),
            ),
          Dom.element(
            "div",
            Dom.attr("class", "form-group"),
            Dom.element(
              "label",
              Dom.attr("for", "elevation"),
              Dom.text("Elevation"),
              ),
            Dom.element(
              "input",
              Dom.attr("type", "number"),
              Dom.attr("class", "form-control"),
              Dom.attr("id", "elevation"),
              Dom.attr("name", "elevation"),
              ),
            ),
          Dom.element(
            "div",
            Dom.attr("class", "form-group"),
            Dom.element(
              "label",
              Dom.attr("for", "difficulty"),
              Dom.text("Difficulty"),
              ),
            Dom.element(
              "input",
              Dom.attr("type", "text"),
              Dom.attr("class", "form-control"),
              Dom.attr("id", "difficulty"),
              Dom.attr("name", "difficulty"),
              ),
            ),
          Dom.element(
            "div",
            Dom.attr("class", "form-group"),
            Dom.element(
              "label",
              Dom.attr("for", "description"),
              Dom.text("Description"),
              ),
            Dom.element(
              "input",
              Dom.attr("type", "text"),
              Dom.attr("class", "form-control"),
              Dom.attr("id", "description"),
              Dom.attr("name", "description"),
              ),
            ),
          Dom.element(
            "div",
            Dom.element(
              "button",
              Dom.attr("type", "submit"),
              Dom.attr("class", "btn btn-primary"),
              Dom.text("Add"),
              Dom.element(
                "script",
                Dom.attr("type", "module"),
                Dom.attr("src", "src/main/scala/view/AlertFromConsole.js"),
              )
            )
          )
        )
      )
    )
  )
}
