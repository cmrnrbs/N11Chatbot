# Kullanılan Teknolojiler

- Dagger + Hilt
- Coroutines & LiveData/Flow
- Coil
- Room
- WebSocket
- MVVM Clean Architecture
- Jetpack Compose

# Çalışma Mantığı

Ekran açılışında ilk olarak Websocket bağlantısı kurulmakta ve step_1 adımı olan bot mesajı ile karşılamaktadır. Room database'inde hiç kayıt yoksa ilk step_1 adımını veritabanına yazıyorum. Bu sayede button seçimlerinden sonra bir sonraki adımı Websocket'e gönderip, echosunu da veritabanına yazıyorum.
Flow ile dinlediğim için bu sayede UI güncellemelerini sağlıyorum. Daha önceki seçimleri hatırlamak amacıyla veritabımda currentStep ve answeredStepName isminde iki adet field oluşturdum. Bu sadece UI tarafında mapleme operasyonunda bunların kontrolünü yaptırıyorum.
Bu bize UX açısından iyi bir deneyim sunmakta. Bu sayede daha önceki seçimlere tekrar tıklayarak socketi mevcut sohbette tekrar meşgul etmiyorlar ve veritabanında duplicate kayıt oluşmasının da önüne geçiyorum.
Konuşmayı sonlandırma aşamasında 2 adım düşündüm. Birincisi kullanıcı kendi isteğiyle yapabilir bir diğeri de artık bot ile yapacağı konuşmanın sonuna gelmiş olabilir. Kendi isteği ile yapacağı görüşmeyi TopAppBar içinde bir action button koyarak gerçekleştirdim. Bir dialog penceresi ile emin onayı aldırıyorum.
Bot ile sonlanan konuşmada ise chat mesajının altına konuşmanın sonlandığına dair bir mesaj ve Tekrar Başlat butonu ekledim. Her iki seçenekte de socket bağlantısı yeniden kurulup veritabanı temizlenmekte. Type değişkeni image olan url için real bir image yok ama ben yine de bunun için Coil kullandım bilginize.
