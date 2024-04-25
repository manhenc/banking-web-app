$(function () {
    var $list = $('#feed-content');
    var data = []; // Define your data source here.

    $list.empty();

    for (var i = 0; i < data.length; i++) {
        $.ajax(data[i].url, {
            accepts: {
                xml: 'application/rss+xml'
            },
            dataType: 'xml'
        })
            .then(function (result) {
                $(result)
                    .find('item')
                    .each(function () {
                        const el = $(this);
                        const template = `
                        <li>
                            <div class="d-inline">
                                <span class="d-block"><b>${el.find("title").text()}</b></span>
                                <span class="d-block">${el.find("description").text().substring(0, 125)}</span>
                            </div>
                        </li>
                    `;
                        $list.append(template);
                    });
            })
            .catch(function (err) {
                console.error(err);
                // Optionally, display an error message on the page.
            });
    }
});
