package com.spinfosec.mq.producer;

import javax.jms.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;

public class MyQueueRequestor
{

	private static final Logger log = LoggerFactory.getLogger(MyQueueRequestor.class);

	private long receiveTimeout = 60000;
	QueueSession session; // The queue session the queue belongs to.
	TemporaryQueue tempQueue;
	QueueSender sender;
	QueueReceiver receiver;

	/**
	 * Constructor for the {@code QueueRequestor} class.
	 * 
	 * <P>
	 * This implementation assumes the session parameter to be non-transacted,
	 * with a delivery mode of either {@code AUTO_ACKNOWLEDGE} or
	 * {@code DUPS_OK_ACKNOWLEDGE}.
	 *
	 * @param session
	 *            the {@code QueueSession} the queue belongs to
	 * @param queue
	 *            the queue to perform the request/reply call on
	 * 
	 * @exception JMSException
	 *                if the JMS provider fails to create the
	 *                {@code QueueRequestor} due to some internal error.
	 * @exception InvalidDestinationException
	 *                if an invalid queue is specified.
	 */

	public MyQueueRequestor(QueueSession session, Queue queue) throws JMSException
	{

		if (queue == null)
			throw new TMCException(RspCode.MQ_QUEUE_NULL);

		this.session = session;
		tempQueue = session.createTemporaryQueue();
		log.debug("临时队列名称：" + tempQueue.getQueueName());
		sender = session.createSender(queue);
		receiver = session.createReceiver(tempQueue);
		log.debug("receiver：" + receiver.getMessageSelector());
	}

	public MyQueueRequestor(QueueSession session, Queue queue, long receiveTimeout) throws JMSException
	{

		if (queue == null)
			throw new TMCException(RspCode.MQ_QUEUE_NULL);
		this.receiveTimeout = receiveTimeout;
		this.session = session;
		tempQueue = session.createTemporaryQueue();
		sender = session.createSender(queue);
		receiver = session.createReceiver(tempQueue);
	}

	/**
	 * Sends a request and waits for a reply. The temporary queue is used for
	 * the {@code JMSReplyTo} destination, and only one reply per request is
	 * expected.
	 * 
	 * @param message
	 *            the message to send
	 * 
	 * @return the reply message
	 * 
	 * @exception JMSException
	 *                if the JMS provider fails to complete the request due to
	 *                some internal error.
	 */

	public Message request(Message message) throws JMSException
	{
		message.setJMSReplyTo(tempQueue);
		sender.send(message);
		log.debug("response replyTo :" + message.getJMSReplyTo());
		return (receiver.receive(receiveTimeout));
	}

	/**
	 * Closes the {@code QueueRequestor} and its session.
	 *
	 * <P>
	 * Since a provider may allocate some resources on behalf of a
	 * {@code QueueRequestor} outside the Java virtual machine, clients should
	 * close them when they are not needed. Relying on garbage collection to
	 * eventually reclaim these resources may not be timely enough.
	 * 
	 * <P>
	 * Note that this method closes the {@code QueueSession} object passed to the
	 * {@code QueueRequestor} constructor.
	 *
	 * @exception JMSException
	 *                if the JMS provider fails to close the
	 *                {@code QueueRequestor} due to some internal error.
	 */

	public void close() throws JMSException
	{

		// publisher and consumer created by constructor are implicitly closed.
		session.close();
		tempQueue.delete();
	}
}