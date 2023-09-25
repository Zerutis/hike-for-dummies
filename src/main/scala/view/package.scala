package dummies

import model.Hike

import zio.http.html._

package object View {

  private val pageConfig: Html = html(
    langAttr := "en",
    head(
      title("Hikes for dummies"),
      link(
        relAttr := "stylesheet",
        hrefAttr := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
      ),
      script(srcAttr := "https://code.jquery.com/jquery-3.5.1.slim.min.js"),
      script(srcAttr := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"),
      script(
        srcAttr := "https://unpkg.com/htmx.org@1.9.6",
        Dom.attr("integrity", "sha384-FhXw7b6AlE/jyjlZH5iHa/tTe9EpJ1Y55RjcgPbjeWMskSxZt1v9qkxLJWNJaGni"),
        Dom.attr("crossorigin", "anonymous"),
        ),
      script(srcAttr := "https://unpkg.com/htmx.org/dist/ext/json-enc.js"),
    ),
  )

  private val titleContent = body(h1("Hikes for dummies using MVC", classAttr := List("text-center")))


  def index(hikes: Seq[Hike]): Html = {
    pageConfig ++ titleContent ++ tableContent(hikes) ++ formContent
  }

  private def tableContent(hikes: Seq[Hike]): Html =
    div(
      idAttr := "table",
      table(
        classAttr := List("table table-striped table-bordered"),
        tHead(tr(th("Name"), th("Distance"), th("Elevation"), th("Difficulty"), th("Description"), th("Actions"))),
        tBody(
          idAttr := "hikes-table",
          Dom.attr("hx-get", "/mvc/hikes"),
          Dom.attr("hx-target", "closest tr"),
          Dom.attr("hx-swap", "outerHTML"),
          hikes.map(
            hike =>
              tr(
                td(hike.name),
                td(hike.distance.toString),
                td(hike.elevation.toString),
                td(hike.difficulty),
                td(hike.description),
                td(
                  button("Edit", typeAttr := "submit", classAttr := List("btn btn-success")),
                  button("Delete", classAttr := List("btn btn-danger"), Dom.attr("hx-delete", s"/mvc/hikes/${hike.id}")),
                )
              )
            )
          )
        )
      )

  def formContent: Html = div(
    classAttr := List("container p-10 my-10 border border-primary rounded"),
    styleAttr := Seq(("width", "50%"), ("margin", "0 auto"), ("margin-top", "20px"), ("margin-bottom", "20px"), ("padding", "20px")),
    h2("Add a new hike", classAttr := List("text-center")),
    form(
      classAttr := List("form-horizontal"),
      Dom.attr("hx-trigger", "submit"),
      Dom.attr("hx-swap", "outerHTML"),
      Dom.attr("hx-target", "#table"),
      Dom.attr("hx-post", "/mvc/hikes"),
      Dom.attr("hx-ext", "json-enc"),
      div(
        label("Id", forAttr := "id"),
        input(
          idAttr := "id",
          nameAttr := "id",
          typeAttr := "number",
          classAttr := List("form-control"),
        ),
        label("Name", forAttr := "name"),
        input(
          idAttr := "name",
          nameAttr := "name",
          typeAttr := "text",
          classAttr := List("form-control"),
          ),
        classAttr := List("form-group"),
      ),
      div(
        label("Distance", forAttr := "distance"),
        input(
          idAttr := "distance",
          nameAttr := "distance",
          typeAttr := "number",
          classAttr := List("form-control"),
          ),
        classAttr := List("form-group"),
      ),
      div(
        label("Elevation", forAttr := "elevation"),
        input(
          idAttr := "elevation",
          nameAttr := "elevation",
          typeAttr := "number",
          classAttr := List("form-control"),
          ),
        classAttr := List("form-group"),
      ),
      div(
        label("Difficulty", forAttr := "difficulty"),
        input(
          idAttr := "difficulty",
          nameAttr := "difficulty",
          typeAttr := "text",
          classAttr := List("form-control"),
          ),
        classAttr := List("form-group"),
      ),
      div(
        label("Description", forAttr := "description"),
        input(
          idAttr := "description",
          nameAttr := "description",
          typeAttr := "text",
          classAttr := List("form-control"),
          ),
        classAttr := List("form-group"),
      ),
      classAttr := List("form-horizontal"),
      div(
        button(
          "Add",
          typeAttr := "submit",
          classAttr := List("btn btn-primary"),
          onClickAttr := "document.getElementById(\"FORM\").reset()",
        ),
      )
    )
  )

  def hikesTable(hikes: Seq[Hike]): Html = tableContent(hikes)
}
