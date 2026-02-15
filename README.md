# Netology Android App - SQL и SQLite
Мобильное приложение для создания и управления постами с использованием SQLite.
## Функциональность
- ✅ Создание, редактирование и удаление постов
- ✅ Лайки и репосты
- ✅ Сохранение черновиков при выходе из формы
- ✅ Хранение данных в SQLite через Room
- ✅ Navigation Component для навигации
- ✅ MVVM архитектура
## Технологии
- Kotlin
- Room Database
- LiveData & ViewModel
- Navigation Component
- ViewBinding
- Coroutines
- Material Design 3
## Требования
- Android Studio Hedgehog | 2023.1.1 или новее
- Kotlin 1.9+
- Gradle 8.0+
- Min SDK 24
- Target SDK 34
## Установка
1. Клонируйте репозиторий
2. Откройте проект в Android Studio
3. Синхронизируйте Gradle
4. Запустите приложение
## Структура базы данных
### Таблица `posts`
- `id` - PRIMARY KEY
- `author` - TEXT
- `content` - TEXT
- `published` - INTEGER (timestamp)
- `likedByMe` - INTEGER (boolean)
- `likes` - INTEGER
- `shares` - INTEGER
- `views` - INTEGER
### Таблица `drafts`
- `id` - PRIMARY KEY (всегда 1)
- `content` - TEXT
- `savedAt` - INTEGER (timestamp)
## Автор
Студент курса Нетология Рустам Мазитов