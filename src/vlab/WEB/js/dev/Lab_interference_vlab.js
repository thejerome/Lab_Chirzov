function init_lab() {
    var MAX_INTENSITY = 4;
    var MIN_INTENSITY = 0;
    var help_slide_number = 0;
    var container;
    var controls_blocked = false;
    var help_active = false;
    var light_color_hex = "";
    var light_color_hex_default = "";
    var laboratory_variant;
    var PLATE_WIDTH = 0;
    var default_plot_data = [
        [0.01, 0],
        [1.01, 0.8]
    ];
    var data_plot_user = [];
    var light_edge;
    var slits_light_edge;
    var default_variant = {
        "light_slits_distance": 0.5,
        "light_screen_distance": 0.7,
        "light_screen_range": [0.01, 2],
        "light_screen_step": 0.01,
        "light_width": 5,
        "light_width_range": [0.1, 10],
        "light_width_step": 0.1,
        "light_length": 721,
        "light_length_range": [380, 780],
        "light_length_step": 1,
        "right_slit_closed": false,
        "left_slit_closed": false,
        "between_slits_width": 1,
        "between_slits_range": [0.1, 2],
        "between_slits_step": 0.1,
        "visibility": 0.2,
        "data_plot_pattern": [
            [0.01, 0.3],
            [10.5, 3.2]
        ]
    };
    var window = '<div class="vlab_setting"><div class="block_title">' +
        '<div class="vlab_name">Виртуальная лаборатория «Опыт Юнга»' +
        '</div><input class="btn_help btn" type="button" value="Справка"/></div>' +
        '<div class="block_field"><div class="block_field_title">Обновление интерференционной картины</div><div class="waiting_loading">' +
        '<img width="100%" height="100%" src="img/Lab_interference_hourglass.png" /></div>' +
        '</div><div class="block_workspace"><div class="workspace_demonstration"><button class="btn btn_play" type="button">' +
        '<img src="img/Lab_interference_play.png" /></button><canvas class="demonstration_light" width="640" height="97"></canvas>' +
        '<div class="demonstration_part part_light"></div><div class="demonstration_part part_slits"></div>' +
        '<div class="demonstration_part part_screen"></div><div class="demonstration_part part_base"></div>' +
        '<label for="control_light_length"> &lambda;: <input class="control_light_length" ' +
        'id="control_light_length" type="range"/><input class="light_length_value" type="number"> нм</label>' +
        '<label for="control_light_slits"> <i>D</i>: <input class="control_light_slits" ' +
        'id="control_light_slits" type="range"/><input class="light_slits_value" type="number"/> м</label>' +
        '<label for="control_slits_screen"> <i>D+d</i>: <input class="control_slits_screen" ' +
        'id="control_slits_screen" type="range"/><input class="slits_screen_value" type="number"/> м</label></div>' +
        '<div class="workspace_light_source"><div class="light_source_screen"><div class="light_source_slit"></div></div><label ' +
        'for="control_light_width"> &alpha;: <input class="control_light_width" id="control_light_width" type="range"/>' +
        '<input class="light_width_value" type="number"/> мм</label>' +
        '</div><div class="workspace_slits">' +
        '<input class="check_left_slit check_slit btn small_btn" slit_id="1" type="checkbox"/><div class="slits_screen">' +
        '<div class="slit slit_1"></div><div class="slit slit_2">' +
        '</div></div><input class="check_right_slit check_slit btn small_btn" slit_id="2" type="checkbox"/>' +
        '<label for="control_slits_width"> <i>A</i>: <input class="control_slits_width" ' +
        'id="control_slits_width" type="range"/><input class="slits_width_value" type="number"/> мм</label>' +
        '</div><div class="workspace_screen">' +
        '<div class="screen_pattern plot_pattern screen_comparison_on screen_user_on"><svg width="240" height="132"></svg></div>' +
        '<div class="screen_user plot_user screen_comparison_on screen_pattern_on"><svg width="240" height="132"></svg></div>' +
        '<div class="screen_pattern_show plot_show not_active" on="screen_pattern" off="screen_pattern_on">Образец</div>' +
        '<div class="screen_user_show plot_show" on="screen_user" off="screen_user_on">Результат</div>' +
        '</div><div class="workspace_intensity_plot"><div class="plot_title">График интенсивности <i>I</i>(<i>x</i>)</div>' +
        '<div class="intensity_plot_pattern plot_pattern intensity_comparison_on intensity_plot_user_on"><svg width="550" height="150"></svg></div>' +
        '<div class="intensity_plot_user plot_user intensity_comparison_on intensity_plot_pattern_on"><svg width="550" height="150"></svg></div>' +
        '<div class="intensity_comparison plot_comparison intensity_plot_user_on intensity_plot_pattern_on"><svg width="550" height="150"></svg></div>' +
        '<div class="intensity_plot_pattern_show plot_show not_active" on="intensity_plot_pattern" off="intensity_plot_pattern_on">Образец</div>' +
        '<div class="intensity_plot_user_show plot_show" on="intensity_plot_user" off="intensity_plot_user_on">Результат</div>' +
        '<div class="intensity_comparison_show plot_show" on="intensity_comparison" off="intensity_comparison_on">Сравнение</div>' +
        '</div><div class="workspace_visibility_plot"><div class="plot_title">Значение видимости <i>V</i></div>' +
        '<div class="visibility_plot_pattern_show">Образец: <div class="visibility_pattern_value"></div></div>' +
        '<div class="visibility_plot_user_show">Результат: <div class="visibility_user_value"></div></div>' +
        '</div></div><div class="block_help">' +
        '<h1>Помощь по работе в виртуальной лаборатории</h1>' +
        '<p>Уважаемый on-line слушатель МООС - курса «Физическая оптика»!</p>' +
        '<p>Вы находитесь в виртуальной оптической лаборатории, где Вашему вниманию предлагается электронная модель ' +
        'классического опыта Юнга, которая гораздо ближе к действительности упрощенной схемы, традиционно изучаемой в ' +
        'элементарных курсах, где как правило рассматривается картина, возникающая при освещении двух бесконечно узких ' +
        'параллельных друг другу щелей монохроматическим светом от источника в виде параллельной щелям бесконечно тонкой ' +
        'самосветящейся нити. В данной работе Вам представляется возможность  познакомиться с ' +
        'моделью опыта Юнга, источник света в которой представляет собой самосветящуюся полоску конечной (и изменяемой ' +
        'пользователем) ширины, что  в большей степени соответствует реальности. Этот источник считается некогерентным, что означает полное отсутствие каких-либо корреляций ' +
        'между излучающими свет колебаниями каждого его малого элемента. Конечно, предлагаемая Вашему вниманию  система тоже ' +
        'является моделью, которая не полностью соответствует действительности. Например, в ней не учтены эффекты, обусловленные ' +
        'конечностью высоты светящейся полоски и конечностью размеров щелей Юнга. Было бы хорошо, если бы Вы попытались ' +
        'создать собственную оригинальную моделирующую программу, реализующую более совершенную модель, учитывающую перечисленные эффекты. ' +
        'Однако знакомство с настоящей электронной моделью тоже весьма поучительно: с ее помощью удается не только продемонстрировать ' +
        'основные особенности «классического» опыта Юнга, но и проиллюстрировать весьма нетривиальные идеи, требующие ' +
        'осознания при изучении вопросов, связанных с пространственной и временной когерентностью реальных источников света.</p>' +
        '<p>Запустите виртуальную  лабораторную  модель.</p>' +
        '<h2>Интерфейс виртуального эксперимента</h2>' +
        '<p>Параметры  модели: &lambda; = 500 нм, <i>D</i> = 0.50  м, <i>D+d</i> = 1.00 м, &alpha; = 0.01 мм,  <i>А</i> = 0.01 мм.</p>' +
        '<div class="help_slide slide_0"><span>1</span><img src="img/Lab_interference_help_1.png" width="429" height="348"/><p>На Вашем компьютере появилась интерактивная ' +
        'модель классического эксперимента по интерференции света, ' +
        'называемого опытом  Юнга. В верхней части рабочего поля изображена схема экспериментальной установки.</p></div>' +
        '<div class="help_slide slide_1"><span>2</span>' +
        '<img class="arrow_green_1" src="img/Lab_interference_arrow_green.png" width="50" height="75"/>' +
        '<img class="arrow_red_1" src="img/Lab_interference_arrow_red.png" width="50" height="75"/>' +
        '<img src="img/Lab_interference_help_1.png" width="429" height="348"/><p>В левой  части  схемы установлен монохроматический некогерентный  источник, ' +
        'длину волны излучения которого можно регулировать движком «&lambda;» во всем оптическом диапазоне от границ ближней ультрафиолетовой ' +
        'области (&lambda; = 380 нм) до границ инфракрасного ' +
        'диапазона (&lambda; = 780 нм). Попытайтесь установить наиболее приятный для Вас цвет светового пучка!</p></div>' +
        '<div class="help_slide slide_2"><span>3</span>' +
        '<img class="arrow_green_1" src="img/Lab_interference_arrow_green.png" width="50" height="75"/>' +
        '<img class="arrow_green_2" src="img/Lab_interference_arrow_green.png" width="50" height="75"/>' +
        '<img class="arrow_red_1" src="img/Lab_interference_arrow_red.png" width="50" height="75"/>' +
        '<img src="img/Lab_interference_help_1.png" width="429" height="348"/><p>Световой пучок от источника освещает щель регулируемой ' +
        'ширины (движок «&alpha;»), каждая точка которой в соответствии с принципом Гюйгенса - Френеля может ' +
        'рассматриваться как вторичный некогерентный источник сферических волн. ' +
        'Изображение так создаваемой самосветящейся полоски выводится в левое окно, расположенное под схемой установки.</p></div>' +
        '<div class="help_slide slide_3"><span>4</span>' +
        '<img class="arrow_green_1" src="img/Lab_interference_arrow_green.png" width="50" height="75"/>' +
        '<img class="arrow_red_1" src="img/Lab_interference_arrow_red.png" width="50" height="75"/>' +
        '<img src="img/Lab_interference_help_4.png" width="429" height="348"/><p>При загрузке эксперимента была установлена ' +
        'минимально возможная для модели ширина полоски - 0.01 мм. Пользуясь движком  «&alpha;», увеличьте размеры полоски до, например, 1 мм.</p></div>' +
        '<div class="help_slide slide_4"><span>5</span>' +
        '<img class="arrow_green_1" src="img/Lab_interference_arrow_green.png" width="50" height="75"/>' +
        '<img class="arrow_red_1" src="img/Lab_interference_arrow_red.png" width="50" height="75"/>' +
        '<img src="img/Lab_interference_help_4.png" width="429" height="348"/><p>Правее вторичного источника света располагается экран с щелями Юнга, положение ' +
        'которого можно изменять с помощью движка «<i>D</i>», перемещая экран вдоль «оптической  скамьи».</div>' +
        '<div class="help_slide slide_5"><span>6</span>' +
        '<img class="arrow_green_1" src="img/Lab_interference_arrow_green.png" width="50" height="75"/>' +
        '<img class="arrow_red_1" src="img/Lab_interference_arrow_red.png" width="50" height="75"/>' +
        '<img src="img/Lab_interference_help_4.png" width="429" height="348"/><p>Расстояние между щелями может регулироваться с помощью  движка «<i>А</i>». ' +
        'Изображение перемещаемых щелей Юнга формируется в среднем окне, расположенном под схемой установки.</p></div>' +
        '<div class="help_slide slide_6"><span>7</span><img src="img/Lab_interference_help_7.png" width="429" height="348"/><p>При загрузке эксперимента ' +
        'было установлено минимально возможное для модели расстояние между щелями - 0.02 мм. ' +
        'С помощью движка «<i>А</i>» увеличьте  его, например, до величины 2.02 мм.</p></div>' +
        '<div class="help_slide slide_7"><span>8</span>' +
        '<img class="arrow_green_1" src="img/Lab_interference_arrow_green.png" width="50" height="75"/>' +
        '<img class="arrow_red_1" src="img/Lab_interference_arrow_red.png" width="50" height="75"/>' +
        '<img src="img/Lab_interference_help_7.png" width="429" height="348"/><p>Экран для наблюдения интерференционной картины ' +
        'располагается правее щелей Юнга и может перемещаться между последним и концом оптической ' +
        'скамьи (движок «<i>D+d</i>»). Обратите внимание на то, что в ' +
        'соответствующем окне отображается расстояние от этого экрана до вторичного источника света, а не щелей Юнга!</p></div>' +
        '<div class="help_slide slide_8"><span>9</span>' +
        '<img class="arrow_green_1" src="img/Lab_interference_arrow_green.png" width="50" height="75"/>' +
        '<img src="img/Lab_interference_help_7.png" width="429" height="348"/><p>Формирующаяся на этом экране интерференционная ' +
        'картина выводится в правое окно, расположенное под схемой установки. Начальные установки были такими, что ' +
        'характерная интерференционная картина была достаточно четкой. Не исключена возможность, что в ' +
        'результате Ваших действий при знакомстве с интерфейсом случилось так, что интерференционная картина уже исчезла.</p></div>' +
        '<div class="help_slide slide_9"><span>10</span>' +
        '<img class="arrow_red_1" src="img/Lab_interference_arrow_red.png" width="50" height="75"/>' +
        '<img src="img/Lab_interference_help_7.png" width="429" height="348"/><p>Для визуализации результатов Ваших действий кликните мышью по пиктограмме, запускающей ' +
        'обновление рабочего экрана. В итоге Вы сможете познакомиться с результатами Ваших усилий по юстировке установки. Если ' +
        'интерференционная картина не появилась - не  огорчайтесь: целью выполнения этой работы является приобретение навыков настройки ' +
        'экспериментальной установки, обеспечивающей получение интерференционной картины с нужными параметрами.</p></div>' +
        '<div class="help_slide slide_10"><span>11</span>' +
        '<img class="arrow_red_1" src="img/Lab_interference_arrow_red.png" width="50" height="75"/>' +
        '<img src="img/Lab_interference_help_7.png" width="429" height="348"/><p>С характерным видом интерференционной картины можно познакомиться, ' +
        'кликнув по левой части ' +
        'переключателя «Образец - результат», расположенного под окном визуализации интерференционной картины.</p></div>' +
        '<div class="help_slide slide_11"><span>12</span>' +
        '<img class="arrow_green_1" src="img/Lab_interference_arrow_green.png" width="50" height="75"/>' +
        '<img class="arrow_red_1" src="img/Lab_interference_arrow_red.png" width="50" height="75"/>' +
        '<img class="arrow_red_2" src="img/Lab_interference_arrow_red.png" width="50" height="75"/>' +
        '<img class="arrow_red_3" src="img/Lab_interference_arrow_red.png" width="50" height="75"/>' +
        '<img src="img/Lab_interference_help_4.png" width="429" height="348"/><p><i>I</i>(<i>x</i>) - график зависимости интенсивности ' +
        'света от координаты экрана наблюдения, он ' +
        'расположен в нижней части рабочего экрана. Находящийся под ним переключатель «Образец - Результат - Сравнение» позволяет ' +
        'поочередно  выводить графики зависимостей <i>I</i>(<i>x</i>) для первоначально устанавливаемых при ' +
        'загрузке параметров оптической системы и текущего  состояния, а также - оба  графика одновременно.</p></div>' +
        '<div class="help_slide slide_12"><span>13</span><img src="img/Lab_interference_help_1.png" width="429" height="348"/><p>' +
        'Теперь у Вас уже достаточно опыта для того, ' +
        'чтобы восстановить начальные настройки и получить исходную интерференционную картину. Настало время выполнить тренировочный эксперимент…</p></div>' +
        '<input class="btn not_active slide_back" type="button" value="Назад" />' +
        '<input class="btn slide_next" type="button" value="Далее" />' +
        '</div></div>';

    function draw_light(color, edge) {
        var canvas = $(".demonstration_light")[0];
        var ctx = canvas.getContext("2d");
        ctx.globalCompositeOperation = 'source-over';
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.fillStyle = '#4f5e6d';
        ctx.save();
        ctx.translate(15, canvas.height - 10);
        ctx.fillRect(0, 0, 35, 10);
        ctx.translate(14.5, -20);
        ctx.fillRect(0, 0, 6, 20);
        ctx.translate(-12.5, -30);
        ctx.fillRect(0, 0, 26, 30);
        ctx.save();
        ctx.fillStyle = color;
        ctx.globalAlpha = 0.6;
        ctx.beginPath();
        ctx.moveTo(26, 24);
        ctx.lineTo(130, 50);
        ctx.lineTo(130 + edge, 50);
        ctx.lineTo(130 + edge, -20);
        ctx.lineTo(130, -20);
        ctx.lineTo(26, 6);
        ctx.closePath();
        ctx.fill();
        ctx.restore();
        ctx.beginPath();
        ctx.moveTo(26, 30);
        ctx.lineTo(35, 35);
        ctx.lineTo(35, -5);
        ctx.lineTo(26, 0);
        ctx.closePath();
        ctx.fill();
        ctx.restore();
    }

    function fill_range(id_range, id_input, value, range, step) {
        $(id_range).attr("min", range[0]);
        $(id_range).attr("max", range[1]);
        $(id_range).attr("step", step);
        $(id_input).attr("min", range[0]);
        $(id_input).attr("max", range[1]);
        $(id_input).attr("step", step);
        $(id_range).val(value);
        $(id_input).val(value);
    }

    function fill_setting(variant) {
        var color_rgb = wavelength_to_rgb(variant.light_length);
        light_color_hex_default = rgb_to_hex(color_rgb);
        fill_range(".control_light_length", ".light_length_value", variant.light_length, variant.light_length_range, variant.light_length_step);
        fill_range(".control_light_width", ".light_width_value", variant.light_width, variant.light_width_range, variant.light_width_step);
        fill_range(".control_slits_width", ".slits_width_value", variant.between_slits_width, variant.between_slits_range, variant.between_slits_step);
        fill_range(".control_light_slits", ".light_slits_value", variant.light_slits_distance, variant.light_screen_range, variant.light_screen_step);
        fill_range(".control_slits_screen", ".slits_screen_value", variant.light_screen_distance, variant.light_screen_range, variant.light_screen_step);
        if (variant.right_slit_closed && variant.left_slit_closed) {
            slits_light_edge = true;
        }
        parse_lightwave_width($(".control_light_width").val(), variant.light_width_range);
        parse_slits_width($(".control_slits_width").val(), variant.between_slits_range);
        parse_light_slits($(".control_light_slits").val(), variant.light_screen_range);
        parse_light_screen($(".control_slits_screen").val(), variant.light_screen_range);
        parse_lightwave_length($(".control_light_length").val());
        if (variant.right_slit_closed) {
            $(".check_right_slit").prop("checked", true);
            $(".slit.slit_2").css("width", 0);
        }
        if (variant.left_slit_closed) {
            $(".check_left_slit").prop("checked", true);
            $(".slit.slit_1").css("width", 0);
        }
        init_plot(variant.data_plot_pattern, ".intensity_plot_pattern svg", 1, 550, 150, 40, 30, 20, false);
        init_interference_picture(variant.data_plot_pattern, ".screen_pattern svg", 240, 132, light_color_hex_default);
        $(".visibility_pattern_value").html(variant.visibility);
    }

    function wavelength_to_rgb(wavelength) {
        var gamma = 0.80,
            intensity_max = 255,
            factor, red, green, blue;
        if ((wavelength >= 380) && (wavelength < 440)) {
            red = -(wavelength - 440) / (440 - 380);
            green = 0.0;
            blue = 1.0;
        } else if ((wavelength >= 440) && (wavelength < 490)) {
            red = 0.0;
            green = (wavelength - 440) / (490 - 440);
            blue = 1.0;
        } else if ((wavelength >= 490) && (wavelength < 510)) {
            red = 0.0;
            green = 1.0;
            blue = -(wavelength - 510) / (510 - 490);
        } else if ((wavelength >= 510) && (wavelength < 580)) {
            red = (wavelength - 510) / (580 - 510);
            green = 1.0;
            blue = 0.0;
        } else if ((wavelength >= 580) && (wavelength < 645)) {
            red = 1.0;
            green = -(wavelength - 645) / (645 - 580);
            blue = 0.0;
        } else if ((wavelength >= 645) && (wavelength < 781)) {
            red = 1.0;
            green = 0.0;
            blue = 0.0;
        } else {
            red = 0.0;
            green = 0.0;
            blue = 0.0;
        }
        if ((wavelength >= 380) && (wavelength < 420)) {
            factor = 0.3 + 0.7 * (wavelength - 380) / (420 - 380);
        } else if ((wavelength >= 420) && (wavelength < 701)) {
            factor = 1.0;
        } else if ((wavelength >= 701) && (wavelength < 781)) {
            factor = 0.3 + 0.7 * (780 - wavelength) / (780 - 700);
        } else {
            factor = 0.0;
        }
        if (red !== 0) {
            red = Math.round(intensity_max * Math.pow(red * factor, gamma));
        }
        if (green !== 0) {
            green = Math.round(intensity_max * Math.pow(green * factor, gamma));
        }
        if (blue !== 0) {
            blue = Math.round(intensity_max * Math.pow(blue * factor, gamma));
        }
        return [red, green, blue];
    }

    function decimal_to_hex(number) {
        var hex = number.toString(16);
        if (hex.length < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    function rgb_to_hex(color) {
        var hexString = '#';
        for (var i = 0; i < 3; i++) {
            hexString += decimal_to_hex(color[i]);
        }
        return hexString;
    }

    function parse_lightwave_length(length_in_nm) {
        var color_rgb = wavelength_to_rgb(length_in_nm);
        light_color_hex = rgb_to_hex(color_rgb);
        $(".light_source_slit").css("background-color", light_color_hex);
        $(".slit.slit_1").css("background-color", light_color_hex);
        $(".slit.slit_2").css("background-color", light_color_hex);
        if (slits_light_edge) {
            light_edge = $(".part_slits").position().left - $(".part_light").position().left;
        } else {
            light_edge = $(".part_screen").position().left - $(".part_light").position().left;
        }
        draw_light(light_color_hex, light_edge);
    }

    function parse_lightwave_width(width_in_m, range) {
        var width_in_percent = (width_in_m / range[1]) * 70 + 1;
        $(".light_source_slit").css("width", width_in_percent + "%");
    }

    function parse_slits_width(width_in_m, range) {
        var width_in_percent = width_in_m / range[1] / 2;
        $(".slit.slit_1").css("left", 49 - width_in_percent * 50 + "%");
        $(".slit.slit_2").css("right", 49 - width_in_percent * 50 + "%");
    }

    function parse_light_slits(width_in_m, range) {
        var width_in_percent = width_in_m / range[1];
        $(".part_slits").css("left", 28 + width_in_percent * 60 + "%");
        if (slits_light_edge) {
            light_edge = $(".part_slits").position().left - $(".part_light").position().left;
        } else {
            light_edge = $(".part_screen").position().left - $(".part_light").position().left;
        }
        draw_light(light_color_hex, light_edge);
    }

    function parse_light_screen(width_in_m, range) {
        var width_in_percent = width_in_m / range[1];
        $(".part_screen").css("left", 28 + width_in_percent * 60 + "%");
        if (slits_light_edge) {
            light_edge = $(".part_slits").position().left - $(".part_light").position().left;
        } else {
            light_edge = $(".part_screen").position().left - $(".part_light").position().left;
        }
        draw_light(light_color_hex, light_edge);
    }

    function show_help() {
        if (!help_active) {
            help_active = true;
            $(".block_help").css("display", "block");
            $(".btn_help").attr("value", "Вернуться");
        } else {
            help_active = false;
            $(".block_help").css("display", "none");
            $(".btn_help").attr("value", "Справка");
        }
    }

    function init_plot(data, plot_selector, y_coefficient, width, height, margin_left, margin_right, margin_bottom, comparison_mode, comparison_data) {
        $(plot_selector).empty();
        var plot = d3.select(plot_selector),
            WIDTH = width,
            HEIGHT = height,
            MARGINS = {
                top: 20,
                right: margin_right,
                bottom: margin_bottom,
                left: margin_left
            };
        var concat_data = data;
        if (comparison_mode) {
            concat_data = data.concat(comparison_data);
        }
        var x_min = d3.min(concat_data, function (d) {
            return d[0];
        });
        var y_min = d3.min(concat_data, function (d) {
            return d[y_coefficient];
        });
        y_min = (y_min < MIN_INTENSITY) ? y_min : MIN_INTENSITY;
        var x_max = d3.max(concat_data, function (d) {
            return d[0];
        });
        var y_max = d3.max(concat_data, function (d) {
            return d[y_coefficient];
        });
        y_max = (y_max > MAX_INTENSITY) ? y_max : MAX_INTENSITY;
        var x_range = d3.scale.linear().range([MARGINS.left, WIDTH - MARGINS.right]).domain([x_min, x_max]);
        var y_range = d3.scale.linear().range([HEIGHT - MARGINS.bottom, MARGINS.bottom]).domain([y_min, y_max]);
        var x_axis = d3.svg.axis()
            .scale(x_range)
            .tickSize(5)
            .tickSubdivide(true);
        var line_func = d3.svg.line()
            .x(function (d) {
                return x_range(d[0]);
            })
            .y(function (d) {
                return y_range(d[y_coefficient]);
            })
            .interpolate('cardinal');
        plot.append("svg:g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + (HEIGHT - MARGINS.bottom) + ")")
            .call(x_axis);
        plot.append("svg:path")
            .attr("d", line_func(data))
            .attr("stroke", light_color_hex)
            .attr("stroke-width", 2)
            .attr("fill", "none");
        var y_axis = d3.svg.axis()
            .scale(y_range)
            .tickSize(5)
            .orient("left")
            .tickSubdivide(true);
        plot.append("svg:g")
            .attr("class", "y axis")
            .attr("transform", "translate(" + (MARGINS.left) + ",0)")
            .call(y_axis);
        if (comparison_mode) {
            plot.append("svg:path")
                .attr("d", line_func(comparison_data))
                .attr("stroke", light_color_hex_default)
                .attr("stroke-width", 2)
                .attr("fill", "none");
        }
    }

    function init_interference_picture(data, picture_selector, width, height, color) {
        $(picture_selector).empty();
        var plot = d3.select(picture_selector),
            WIDTH = width,
            HEIGHT = height;
        plot.selectAll("rect")
            .data(data)
            .enter()
            .append("rect")
            .attr("x", function(d, i) {
                return i * (WIDTH / data.length);
            })
            .attr("y", function(d) {
                return 0;
            })
            .attr("width", WIDTH / data.length)
            .attr("height", function(d) {
                return HEIGHT;
            })
            .attr("fill", color)
            .style("opacity", function(d) {
                return d[1]/MAX_INTENSITY;
            });
    }

    function launch() {
        controls_blocked = true;
        $(".block_field").addClass("active_waiting");
        ANT.calculate();
    }

    function change_length_range() {
        $(".light_length_value").val($(".control_light_length").val());
        parse_lightwave_length($(".control_light_length").val());
    }

    function change_length_input() {
        if ($.isNumeric($(".light_length_value").val())) {
            if (($(".light_length_value").val() <= laboratory_variant.light_length_range[1]) &
                ($(".light_length_value").val() >= laboratory_variant.light_length_range[0])) {
                $(".control_light_length").val($(".light_length_value").val());
                $(".light_length_value").val($(".control_light_length").val());
            } else {
                if ($(".light_length_value").val() > laboratory_variant.light_length_range[1]) {
                    $(".control_light_length").val(laboratory_variant.light_length_range[1]);
                    $(".light_length_value").val(laboratory_variant.light_length_range[1]);
                } else {
                    if ($(".light_length_value").val() < laboratory_variant.light_length_range[0]) {
                        $(".control_light_length").val(laboratory_variant.light_length_range[0]);
                        $(".light_length_value").val(laboratory_variant.light_length_range[0]);
                    }
                }
            }
        } else {
            $(".control_light_length").val(laboratory_variant.light_length_range[0]);
            $(".light_length_value").val(laboratory_variant.light_length_range[0]);
        }
        parse_lightwave_length($(".light_length_value").val());
    }

    function change_width_range() {
        $(".light_width_value").val($(".control_light_width").val());
        parse_lightwave_width($(".control_light_width").val(), laboratory_variant.light_width_range);
    }

    function change_width_input() {
        if ($.isNumeric($(".light_width_value").val())) {
            if (($(".light_width_value").val() <= laboratory_variant.light_width_range[1]) &
                ($(".light_width_value").val() >= laboratory_variant.light_width_range[0])) {
                $(".control_light_width").val($(".light_width_value").val());
                $(".light_width_value").val($(".control_light_width").val());
            } else {
                if ($(".light_width_value").val() > laboratory_variant.light_width_range[1]) {
                    $(".control_light_width").val(laboratory_variant.light_width_range[1]);
                    $(".light_width_value").val(laboratory_variant.light_width_range[1]);
                } else {
                    if ($(".light_width_value").val() < laboratory_variant.light_width_range[0]) {
                        $(".control_light_width").val(laboratory_variant.light_width_range[0]);
                        $(".light_width_value").val(laboratory_variant.light_width_range[0]);
                    }
                }
            }
        } else {
            $(".control_light_width").val(laboratory_variant.light_width_range[0]);
            $(".light_width_value").val(laboratory_variant.light_width_range[0]);
        }
        parse_lightwave_width($(".light_width_value").val(), laboratory_variant.light_width_range);
    }

    function change_slits_range() {
        $(".slits_width_value").val($(".control_slits_width").val());
        parse_slits_width($(".control_slits_width").val(), laboratory_variant.between_slits_range);
    }

    function change_slits_input() {
        if ($.isNumeric($(".slits_width_value").val())) {
            if (($(".slits_width_value").val() <= laboratory_variant.between_slits_range[1]) &
                ($(".slits_width_value").val() >= laboratory_variant.between_slits_range[0])) {
                $(".control_slits_width").val($(".slits_width_value").val());
                $(".slits_width_value").val($(".control_slits_width").val());
            } else {
                if ($(".slits_width_value").val() > laboratory_variant.between_slits_range[1]) {
                    $(".control_slits_width").val(laboratory_variant.between_slits_range[1]);
                    $(".slits_width_value").val(laboratory_variant.between_slits_range[1]);
                } else {
                    if ($(".slits_width_value").val() < laboratory_variant.between_slits_range[0]) {
                        $(".control_slits_width").val(laboratory_variant.between_slits_range[0]);
                        $(".slits_width_value").val(laboratory_variant.between_slits_range[0]);
                    }
                }
            }
        } else {
            $(".control_slits_width").val(laboratory_variant.between_slits_range[0]);
            $(".slits_width_value").val(laboratory_variant.between_slits_range[0]);
        }
        parse_slits_width($(".control_slits_width").val(), laboratory_variant.between_slits_range);
    }

    function change_light_slits_range() {
        if ($(".control_light_slits").val() >= (parseFloat($(".control_slits_screen").val()) - PLATE_WIDTH)) {
            $(".control_light_slits").val(parseFloat($(".control_slits_screen").val()) - PLATE_WIDTH - laboratory_variant.light_screen_step);
        }
        $(".light_slits_value").val($(".control_light_slits").val());
        parse_light_slits($(".control_light_slits").val(), laboratory_variant.light_screen_range);
    }

    function change_light_slits_input() {
        if ($.isNumeric($(".light_slits_value").val())) {
            if ($(".light_slits_value").val() >= (parseFloat($(".control_slits_screen").val()) - PLATE_WIDTH)) {
                $(".light_slits_value").val(parseFloat($(".control_slits_screen").val()) - PLATE_WIDTH - laboratory_variant.light_screen_step);
            }
            if (($(".light_slits_value").val() <= laboratory_variant.light_screen_range[1]) &
                ($(".light_slits_value").val() >= laboratory_variant.light_screen_range[0])) {
                $(".control_light_slits").val($(".light_slits_value").val());
                $(".light_slits_value").val($(".control_light_slits").val());
            } else {
                if ($(".light_slits_value").val() > laboratory_variant.light_screen_range[1]) {
                    $(".control_light_slits").val(laboratory_variant.light_screen_range[1]);
                    $(".light_slits_value").val(laboratory_variant.light_screen_range[1]);
                } else {
                    if ($(".light_slits_value").val() < laboratory_variant.light_screen_range[0]) {
                        $(".control_light_slits").val(laboratory_variant.light_screen_range[0]);
                        $(".light_slits_value").val(laboratory_variant.light_screen_range[0]);
                    }
                }
            }
        } else {
            $(".control_light_slits").val(laboratory_variant.light_screen_range[0]);
            $(".light_slits_value").val(laboratory_variant.light_screen_range[0]);
        }
        parse_light_slits($(".control_light_slits").val(), laboratory_variant.light_screen_range);
    }

    function change_slits_screen_range() {
        if ($(".control_slits_screen").val() <= (parseFloat($(".control_light_slits").val()) + PLATE_WIDTH)) {
            $(".control_slits_screen").val(parseFloat($(".control_light_slits").val()) + PLATE_WIDTH + laboratory_variant.light_screen_step);
        }
        $(".slits_screen_value").val($(".control_slits_screen").val());
        parse_light_screen($(".control_slits_screen").val(), laboratory_variant.light_screen_range);
    }

    function change_slits_screen_input() {
        if ($.isNumeric($(".slits_screen_value").val())) {
            if ($(".slits_screen_value").val() <= (parseFloat($(".control_light_slits").val()) + PLATE_WIDTH)) {
                $(".slits_screen_value").val(parseFloat($(".control_light_slits").val()) + PLATE_WIDTH + laboratory_variant.light_screen_step);
            }
            if (($(".slits_screen_value").val() <= laboratory_variant.light_screen_range[1]) &
                ($(".slits_screen_value").val() >= laboratory_variant.light_screen_range[0])) {
                $(".control_slits_screen").val($(".slits_screen_value").val());
                $(".slits_screen_value").val($(".control_slits_screen").val());
            } else {
                if ($(".slits_screen_value").val() > laboratory_variant.light_screen_range[1]) {
                    $(".control_slits_screen").val(laboratory_variant.light_screen_range[1]);
                    $(".slits_screen_value").val(laboratory_variant.light_screen_range[1]);
                } else {
                    if ($(".slits_screen_value").val() < laboratory_variant.light_screen_range[0]) {
                        $(".control_slits_screen").val(laboratory_variant.light_screen_range[0]);
                        $(".slits_screen_value").val(laboratory_variant.light_screen_range[0]);
                    }
                }
            }
        } else {
            $(".control_slits_screen").val(laboratory_variant.light_screen_range[0]);
            $(".slits_screen_value").val(laboratory_variant.light_screen_range[0]);
        }
        parse_light_screen($(".control_slits_screen").val(), laboratory_variant.light_screen_range);
    }

    function parse_result(str, default_object) {
        var parsed_object;
        if (typeof str === 'string' && str !== "") {
            try {
                parsed_object = str.replace(/<br\/>/g, "\r\n").replace(/&amp;/g, "&").replace(/&quot;/g, "\"").replace(/&lt;br\/&gt;/g, "\r\n")
                    .replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&minus;/g, "-").replace(/&apos;/g, "\'").replace(/&#0045;/g, "-")
                    .replace(/!/g, "\"").replace(/\$/g, "-");
                parsed_object = JSON.parse(parsed_object);
            } catch (e) {
                if (default_object){
                    parsed_object = default_object;
                } else {
                    parsed_object = false;
                }
            }
        } else {
            if (default_object){
                parsed_object = default_object;
            } else {
                parsed_object = false;
            }
        }
        return parsed_object;
    }

    function get_variant() {
        var variant;
        if ($("#preGeneratedCode") !== null) {
            variant = parse_result($("#preGeneratedCode").val(), default_variant);
        } else {
            variant = default_variant;
        }
        return variant;
    }

    function change_slits_status(that) {
        var slit_id = ".slit_" + $(that).attr("slit_id");
        if ($(that).prop("checked") === true) {
            $(slit_id).css("width", "0");
        } else {
            $(slit_id).css("width", "1px");
        }
        if ($(".check_left_slit").prop("checked") && $(".check_right_slit").prop("checked")) {
            slits_light_edge = true;
            light_edge = $(".part_slits").position().left - $(".part_light").position().left;
        } else {
            slits_light_edge = false;
            light_edge = $(".part_screen").position().left - $(".part_light").position().left;
        }
        draw_light(light_color_hex, light_edge);
    }

    function draw_previous_solution(previous_solution){
        $(".control_light_length").val(previous_solution.light_length);
        change_length_range();
        $(".control_light_width").val(previous_solution.light_width);
        change_width_range();
        $(".control_slits_width").val(previous_solution.between_slits_width);
        change_slits_range();
        $(".control_light_slits").val(previous_solution.light_slits_distance);
        change_light_slits_range();
        $(".control_slits_screen").val(previous_solution.light_screen_distance);
        change_slits_screen_range();
        if (previous_solution.left_slit_closed){
            change_slits_status($(".check_left_slit"));
        }
        if (previous_solution.right_slit_closed){
            change_slits_status($(".check_right_slit"));
        }
    }

    return {
        init: function () {
            laboratory_variant = get_variant();
            container = $("#jsLab")[0];
            container.innerHTML = window;
            fill_setting(laboratory_variant);
            if ($("#previousSolution") !== null && $("#previousSolution").length > 0 && parse_result($("#previousSolution").val())) {
                var previous_solution = parse_result($("#previousSolution").val());
                draw_previous_solution(previous_solution);
            }
            $(".btn_help").click(function () {
                show_help();
            });
            $(".control_light_length").on("change mousemove", function () {
                if (!controls_blocked) {
                    change_length_range();
                }
            });
            $(".light_length_value").change(function () {
                if (!controls_blocked) {
                    change_length_input();
                }
            });
            $(".control_light_width").on("change mousemove", function () {
                if (!controls_blocked) {
                    change_width_range();
                }
            });
            $(".light_width_value").change(function () {
                if (!controls_blocked) {
                    change_width_input();
                }
            });
            $(".check_slit").on("click", function () {
                if (!controls_blocked) {
                    change_slits_status(this);
                }
            });
            $(".control_slits_width").on("change mousemove", function () {
                if (!controls_blocked) {
                    change_slits_range();
                }
            });
            $(".slits_width_value").change(function () {
                if (!controls_blocked) {
                    change_slits_input();
                }
            });
            $(".control_light_slits").on("change mousemove", function () {
                if (!controls_blocked) {
                    change_light_slits_range();
                }
            });
            $(".light_slits_value").change(function () {
                if (!controls_blocked) {
                    change_light_slits_input();
                }
            });
            $(".control_slits_screen").on("change mousemove", function () {
                if (!controls_blocked) {
                    change_slits_screen_range();
                }
            });
            $(".slits_screen_value").change(function () {
                if (!controls_blocked) {
                    change_slits_screen_input();
                }
            });
            $(".plot_show").click(function () {
                if (!controls_blocked) {
                    $(this).siblings(".not_active").removeClass("not_active");
                    $(this).addClass("not_active");
                    $("." + $(this).attr("off")).css("display", "none");
                    $("." + $(this).attr("on")).css("display", "block");
                }
            });
            $(".btn_play").click(function () {
                if (!controls_blocked) {
                    launch();
                }
            });
            $(".slide_next").click(function () {
                if (help_slide_number < 12) {
                    help_slide_number ++;
                    $(".slide_back").removeClass("not_active");
                    $(".help_slide").css("display", "none");
                    $(".help_slide.slide_" + help_slide_number).css("display", "block");
                    if (help_slide_number === 12) {
                        $(this).addClass("not_active")
                    }
                }
            });
            $(".slide_back").click(function () {
                if (help_slide_number > 0) {
                    help_slide_number --;
                    $(".slide_next").removeClass("not_active");
                    $(".help_slide").css("display", "none");
                    $(".help_slide.slide_" + help_slide_number).css("display", "block");
                    if (help_slide_number === 0) {
                        $(this).addClass("not_active")
                    }
                }
            })
        },
        calculateHandler: function () {
            data_plot_user = parse_result(arguments[0], default_plot_data);
            init_plot(data_plot_user.data_plot, ".intensity_plot_user svg", 1, 550, 150, 40, 30, 20, false);
            init_plot(data_plot_user.data_plot, ".intensity_comparison svg", 1, 550, 150, 40, 30, 20, true, laboratory_variant.data_plot_pattern);
            init_interference_picture(data_plot_user.data_plot, ".screen_user svg", 240, 132, light_color_hex);
            $(".visibility_user_value").html(data_plot_user.visibility);
            $(".plot_pattern").css("display", "none");
            $(".plot_comparison").css("display", "none");
            $(".plot_user").css("display", "block");
            $(".plot_show").removeClass("not_active");
            $(".screen_user_show").addClass("not_active");
            $(".intensity_plot_user_show").addClass("not_active");
            $(".block_field").removeClass("active_waiting");
            controls_blocked = false;
        },
        getResults: function () {
            var answer = {};
            answer.light_slits_distance = parseFloat($(".control_light_slits").val());
            answer.light_screen_distance = parseFloat($(".control_slits_screen").val());
            answer.light_width = parseFloat($(".control_light_width").val());
            answer.light_length = parseFloat($(".control_light_length").val());
            answer.left_slit_closed = ($(".check_left_slit").prop("checked") === true);
            answer.right_slit_closed = ($(".check_right_slit").prop("checked") === true);
            answer.between_slits_width = parseFloat($(".control_slits_width").val());
            answer = JSON.stringify(answer);
            return answer;
        },
        getCondition: function () {
            var condition;
            condition = "";
            return condition;
        }
    }
}

var Vlab = init_lab();