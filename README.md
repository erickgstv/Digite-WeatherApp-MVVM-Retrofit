# WeatherApp - Previsão do Tempo 🌤️

Um aplicativo Android moderno e responsivo que fornece informações meteorológicas em tempo real utilizando a API do OpenWeatherMap. Este projeto foi desenvolvido para demonstrar competências em arquitetura Android, consumo de APIs e integração com hardware (GPS).

## 🚀 Funcionalidades

- **Busca por Cidade:** Consulta global de dados meteorológicos.
- **Localização via GPS:** Obtenção automática da previsão baseada nas coordenadas atuais.
- **Persistência Local:** Armazenamento da última cidade pesquisada com `SharedPreferences` para facilitar o próximo acesso.
- **Interface Dinâmica:** Mudança automática de cores de fundo e ícones baseada na condição climática (Sol, Nuvens, Chuva).
- **Informações Completas:** Exibição de temperatura, sensação térmica, umidade e velocidade do vento.
- **Feedback de UX:** Indicador de progresso (ProgressBar), fechamento automático do teclado e tratamento de erros profissional.

## 🛠️ Tecnologias e Bibliotecas

- **Linguagem:** [Kotlin](https://kotlinlang.org/)
- **Networking:** [Retrofit](https://square.github.io/retrofit/) & [GSON](https://github.com/google/gson)
- **Arquitetura:** MVVM (Model-View-ViewModel)
- **Jetpack Components:** LiveData, ViewModel
- **Localização:** LocationManager API
- **UI:** ConstraintLayout, Material Design, Vector Drawables

## 📐 Arquitetura

O projeto utiliza o padrão **MVVM**, garantindo a separação de responsabilidades:
- **Model:** Classes de dados que representam a resposta da API e lógica de mapeamento de recursos.
- **View:** Atividade e layouts XML focados na exibição e interação do usuário.
- **ViewModel:** Gerencia o estado da UI e coordena as chamadas à API, mantendo os dados seguros durante mudanças de configuração.

## ⚙️ Como configurar

1. Clone o repositório.
2. Obtenha uma chave de API gratuita no site [OpenWeatherMap](https://openweathermap.org/).
3. No arquivo `MainActivity.kt`, insira sua chave na variável correspondente:
   ```kotlin
   private val apiKey = "SUA_API_KEY_AQUI"
   ```

    <img width="1080" height="2110" alt="WhatsApp Image 2026-05-13 at 15 51 58" src="https://github.com/user-attachments/assets/1f1e40a0-409a-45a3-928e-99d3c4157627" />

4. Compile e execute o projeto através do Android Studio.

---
Projeto desenvolvido para fins de portfólio técnico.
