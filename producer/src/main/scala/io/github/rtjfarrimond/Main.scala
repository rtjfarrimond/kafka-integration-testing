package io.github.rtjfarrimond

import cats.effect.Async
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.effect.Sync

import fs2.Stream
import fs2.kafka.AdminClientSettings
import fs2.kafka.KafkaAdminClient
import fs2.kafka.KafkaProducer
import fs2.kafka.ProducerRecord
import fs2.kafka.ProducerRecords
import fs2.kafka.ProducerSettings

import org.apache.kafka.clients.admin.NewTopic


object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    val bootstrapServers = "localhost:9092" // TODO: Config

    val producerSettings = ProducerSettings[IO, Int, Int]
      .withBootstrapServers(bootstrapServers)

    val recordStream = Stream.emits(0 until 10).map { i =>
        val record = ProducerRecord("bar", i, i+1)
        ProducerRecords.one(record)

    }.through(KafkaProducer.pipe(producerSettings))

    val topicName = "super-duper-kafka-topic"
    val numPartitions = 12
    val replicationFactor = 1

    // for {
    //   _ <- echo[IO](s"Creating topic '$topicName'...")
    //   _ <- createTopic[IO](kafkaAdminClientResource)(
    //           topicName,
    //           numPartitions,
    //           replicationFactor)
    //   _ <- echo[IO]("Producing records...")
    //   _ <- recordStream.compile.drain
    // } yield ExitCode.Success

    kafkaAdminClientResource[IO](bootstrapServers).use { client =>
      for {
        // _ <- echo[IO](s"Creating topic '$topicName'...")
        // _ <- client.createTopic(new NewTopic(topicName, numPartitions, replicationFactor.toShort))
        topicDetails  <- client.describeTopics(List(topicName))
        _             <- IO(topicDetails.values.map(println))
      } yield ExitCode.Success
    }

  }

  def kafkaAdminClientResource[F[_]: Async](
    bootstrapServers: String
  ): Resource[F, KafkaAdminClient[F]] =
    KafkaAdminClient.resource[F](AdminClientSettings(bootstrapServers))

  private def createTopic[F[_]: Sync](adminClient: Resource[F, KafkaAdminClient[F]])(topicName: String, numPartitions: Int, replicationFactor: Int): F[Unit] = {
    adminClient.use { client =>
      client.createTopic(new NewTopic(topicName, numPartitions, replicationFactor.toShort))
    }
  }

  private def echo[F[_]](s: String)(implicit sync: Sync[F]): F[Unit] =
    sync.delay(println(s))

}
