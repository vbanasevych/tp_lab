# Changelog

Усі помітні зміни в цьому проєкті документуються у цьому файлі.
Формат базується на [Keep a Changelog](https://keepachangelog.com/uk/1.1.0/), а версіонування відповідає [Semantic Versioning](https://semver.org/).

## [1.0.0] - 2026-11-14

### Added
- Повна документація користувача (`README.md`) та інструкції запуску CLI.
- Автоматизований пайплайн створення GitHub Releases за семантичними тегами (`v*.*.*`).
- Конфігурація збірки та публікації пакета (`sources`, `javadoc`, Maven Central publishing workflow).

## [0.4.0] - 2026-11-12

### Added
- `ParseTreeVisualizer` та CLI-команда `vis-parse` для експорту дерева синтаксичного виводу у формати DOT (Graphviz) та JSON.
- `AstVisualizer` та CLI-команда `vis-ast` для візуалізації очищеного AST у DOT/JSON та структурного порівняння з Parse Tree.

## [0.3.0] - 2026-11-12

### Added
- Подієва модель виконання (`ExecutionListener`) з хуками `onBeforeStatement` та `onAfterStatement`.
- CLI-інтерфейс на базі Picocli (`SiplCli`) з командами `parse`, `check`, `run`.
- Команда `trace` для покрокового трасування стану пам'яті у форматі JSON.

## [0.2.0] - 2026-11-11

### Added
- Інтерпретатор SIPL (`Interpreter`) на базі Java Pattern Matching для `switch`.
- Підтримка стану змінних (`Map<String, Integer>`), обчислення арифметичних (`Aexp`) і булевих (`Bexp`) виразів та виконання операторів (`:=`, `;`, `if`, `while`, `begin/end`, `skip`).

## [0.1.0] - 2026-11-10

### Added
- Формальна граматика SIPL для ANTLR4 (`SIPL.g4`).
- Набір позитивних та негативних модульних тестів синтаксису (`SIPLParserTest`).
- Типізоване AST (`AstNode`, `Stm`, `Aexp`, `Bexp`) із збереженням координат (`SourceSpan`) та `AstBuilderVisitor`.
- Базовий CI-пайплайн у GitHub Actions (`ci.yml`).