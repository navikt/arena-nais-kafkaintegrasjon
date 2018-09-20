package no.nav.arena.nais.kafkaintegrasjon.kafka;

public class KafkaMessage {
    private String text;
    private String topic;

    public KafkaMessage() { }

    public KafkaMessage(String text, String topic) {
        this.text = text;
        this.topic = topic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
