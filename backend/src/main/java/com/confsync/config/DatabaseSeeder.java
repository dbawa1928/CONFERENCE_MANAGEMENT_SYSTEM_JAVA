package com.confsync.config;

import com.confsync.entity.*;
import com.confsync.enums.*;
import com.confsync.repository.*;
import com.confsync.util.CertificatePdfGenerator;
import com.confsync.util.QrCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Initializes persistent demo data in MySQL on first run.
 * Populates realistic users, conferences, sessions, speakers, papers, and reviews.
 */
@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private ReviewerAssignmentRepository assignmentRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private QrCodeGenerator qrCodeGenerator;

    @Autowired
    private CertificatePdfGenerator pdfGenerator;

    @Value("${file.upload-certificates-dir:uploads/certificates}")
    private String certificatesDir;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return; // Data already exists in database
        }

        System.out.println("🌱 ConfSync: Seeding initial demo data into MySQL...");

        // 1. Seed Users
        User admin = userRepository.save(new User(
                "Dr. Eleanor Vance (Admin)",
                "admin@confsync.com",
                passwordEncoder.encode("admin123"),
                Role.ADMIN
        ));

        User participant1 = userRepository.save(new User(
                "John Doe",
                "john.doe@university.edu",
                passwordEncoder.encode("password123"),
                Role.PARTICIPANT
        ));

        User participant2 = userRepository.save(new User(
                "Sarah Connor",
                "sarah.connor@mit.edu",
                passwordEncoder.encode("password123"),
                Role.PARTICIPANT
        ));

        User speakerUser1 = userRepository.save(new User(
                "Prof. Alan Turing",
                "alan.turing@cambridge.edu",
                passwordEncoder.encode("password123"),
                Role.SPEAKER
        ));

        User speakerUser2 = userRepository.save(new User(
                "Dr. Grace Hopper",
                "grace.hopper@navy.edu",
                passwordEncoder.encode("password123"),
                Role.SPEAKER
        ));

        User author1 = userRepository.save(new User(
                "Alex Chen",
                "alex.chen@stanford.edu",
                passwordEncoder.encode("password123"),
                Role.AUTHOR
        ));

        User author2 = userRepository.save(new User(
                "Dr. Maria Garcia",
                "maria.garcia@oxford.edu",
                passwordEncoder.encode("password123"),
                Role.AUTHOR
        ));

        User reviewer1 = userRepository.save(new User(
                "Prof. David Miller",
                "david.miller@berkeley.edu",
                passwordEncoder.encode("password123"),
                Role.REVIEWER
        ));

        User reviewer2 = userRepository.save(new User(
                "Dr. Priya Sharma",
                "priya.sharma@iitd.ac.in",
                passwordEncoder.encode("password123"),
                Role.REVIEWER
        ));

        // 2. Seed Speakers
        Speaker speaker1 = speakerRepository.save(new Speaker(
                speakerUser1,
                "Cambridge Computer Laboratory",
                "Chair of Artificial Intelligence",
                "World-renowned researcher in algorithmic reasoning, automated theorem proving, and machine intelligence.",
                "Artificial Intelligence, Quantum Computing, Formal Methods",
                "+1 (555) 234-5678"
        ));

        Speaker speaker2 = speakerRepository.save(new Speaker(
                speakerUser2,
                "Systems & Cloud Architecture Labs",
                "Distinguished Systems Architect",
                "Pioneer in high-throughput distributed systems, compiler design, and resilient cloud services.",
                "Cloud Systems, Microservices, Distributed Databases",
                "+1 (555) 876-5432"
        ));

        // 3. Seed Conferences
        Conference conf1 = conferenceRepository.save(new Conference(
                "ICAI 2027: International Conference on Artificial Intelligence",
                "The premier international forum for presenting state-of-the-art research in Artificial Intelligence, Deep Learning, and Responsible Machine Learning applications.",
                "Artificial Intelligence",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1).plusDays(20),
                "Grand Convention Center, Cambridge, MA",
                300,
                ConferenceStatus.UPCOMING
        ));

        Conference conf2 = conferenceRepository.save(new Conference(
                "CloudScale Summit 2027: Cloud-Native & Microservices",
                "A world-class summit gathering distributed systems engineers, cloud architects, and research pioneers to discuss next-generation infrastructure scalability.",
                "Cloud Computing",
                LocalDate.now().plusMonths(3),
                LocalDate.now().plusMonths(3).plusDays(2),
                LocalDate.now().plusMonths(2).plusDays(15),
                "Metropolitan Tech Pavilion, Seattle, WA",
                450,
                ConferenceStatus.UPCOMING
        ));

        Conference conf3 = conferenceRepository.save(new Conference(
                "CyberSec World 2027: Global Cryptography & Defense",
                "Leading global conference addressing zero-trust architectures, post-quantum cryptography, threat intelligence, and modern defensive paradigms.",
                "Cybersecurity",
                LocalDate.now().plusMonths(4),
                LocalDate.now().plusMonths(4).plusDays(3),
                LocalDate.now().plusMonths(3).plusDays(10),
                "Cyber Tech Arena, San Jose, CA",
                350,
                ConferenceStatus.UPCOMING
        ));

        // 4. Seed Sessions
        sessionRepository.save(new Session(
                conf1,
                speaker1,
                "Keynote: The Frontier of Multi-Modal Reasoning Models",
                "An opening keynote exploring how modern large models align factual representation with rigorous mathematical logic.",
                conf1.getStartDate(),
                LocalTime.of(9, 30),
                LocalTime.of(11, 0),
                "Main Auditorium A",
                "Artificial Intelligence",
                300
        ));

        sessionRepository.save(new Session(
                conf1,
                speaker2,
                "Panel: Scaling Large Models Safely and Efficiently",
                "Interactive technical discussion on compute bottlenecks, distributed training clusters, and memory optimization.",
                conf1.getStartDate().plusDays(1),
                LocalTime.of(14, 0),
                LocalTime.of(15, 30),
                "Hall C - Research Wing",
                "Machine Learning Systems",
                150
        ));

        sessionRepository.save(new Session(
                conf2,
                speaker2,
                "Masterclass: Microservice Resilience with Kernel-Level Mesh",
                "Hands-on architectural masterclass showcasing real-world performance benchmarks and zero-packet loss configurations.",
                conf2.getStartDate(),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                "Expo Hall 1",
                "Cloud Architecture",
                200
        ));

        // 5. Seed Papers
        Paper paper1 = paperRepository.save(new Paper(
                author1,
                conf1,
                "Reinforcement Learning with Explainable Decision Forests for Diagnostics",
                "Medical diagnostic pipelines require rigorous interpretability alongside statistical accuracy. In this paper, we propose a hybrid model coupling policy gradient reinforcement learning with decision forest explanations.",
                "Artificial Intelligence",
                "Reinforcement Learning, Healthcare, Decision Forests, Explainable AI",
                null
        ));
        paper1.setStatus(PaperStatus.UNDER_REVIEW);
        paperRepository.save(paper1);

        Paper paper2 = paperRepository.save(new Paper(
                author2,
                conf2,
                "Zero-Latency Microservice Communication via Kernel-Level BPF Acceleration",
                "Standard sidecar proxies in Kubernetes clusters introduce 15-25% tail-latency overhead. This work introduces an autonomous socket-layer eBPF filter that reduces round-trip packet latency to near hardware limits.",
                "Cloud Computing",
                "eBPF, Microservices, Kubernetes, High-Performance Networking",
                null
        ));
        paper2.setStatus(PaperStatus.ACCEPTED);
        paperRepository.save(paper2);

        // 6. Seed Reviewer Assignments and Reviews
        ReviewerAssignment assign1 = assignmentRepository.save(new ReviewerAssignment(paper1, reviewer1));
        ReviewerAssignment assign2 = assignmentRepository.save(new ReviewerAssignment(paper2, reviewer2));
        assign2.setStatus(AssignmentStatus.COMPLETED);
        assignmentRepository.save(assign2);

        // Review for Paper 2
        reviewRepository.save(new Review(
                paper2,
                reviewer2,
                9.5,
                9.0,
                9.5,
                9.0,
                "Outstanding contribution! The experimental evaluation is rigorous, and the open-source benchmarks validate the claimed latency reduction.",
                Recommendation.ACCEPT
        ));

        // 7. Seed Registrations & Attendance
        String qrPayload1 = "CS-REG-" + conf1.getId() + "-" + participant1.getId() + "-DEMO1";
        String qrImage1 = qrCodeGenerator.generateQrCodeBase64(qrPayload1, 300, 300);
        Registration reg1 = registrationRepository.save(new Registration(participant1, conf1, qrImage1));

        String qrPayload2 = "CS-REG-" + conf2.getId() + "-" + participant2.getId() + "-DEMO2";
        String qrImage2 = qrCodeGenerator.generateQrCodeBase64(qrPayload2, 300, 300);
        Registration reg2 = registrationRepository.save(new Registration(participant2, conf2, qrImage2));
        reg2.setAttendanceStatus(true);
        registrationRepository.save(reg2);

        // Attendance Record for Sarah Connor
        attendanceRepository.save(new Attendance(reg2));

        // Certificate for Sarah Connor
        String certNum = "CS-CERT-" + conf2.getId() + "-" + participant2.getId() + "-1001";
        String verifyCode = "CONF-CERT-9821";
        File certFile = Paths.get(certificatesDir, "Certificate_" + certNum + ".pdf").toFile();
        try {
            pdfGenerator.generateCertificatePdf(
                    certFile,
                    participant2.getName(),
                    conf2.getTitle(),
                    conf2.getStartDate().toString() + " - " + conf2.getEndDate().toString(),
                    certNum,
                    verifyCode
            );
        } catch (Exception e) {
            System.err.println("Could not pre-generate PDF: " + e.getMessage());
        }

        certificateRepository.save(new Certificate(
                participant2,
                conf2,
                certNum,
                verifyCode,
                certFile.getAbsolutePath()
        ));

        // 8. Seed Notifications
        notificationRepository.save(new Notification(
                admin,
                "System Initialized",
                "ConfSync Conference Management System is operational and running on localhost."
        ));

        notificationRepository.save(new Notification(
                participant1,
                "Registration Confirmed",
                "You are registered for ICAI 2027. Your QR check-in pass is available in your dashboard."
        ));

        notificationRepository.save(new Notification(
                participant2,
                "Certificate Awarded!",
                "Congratulations! Your official Certificate of Participation for CloudScale Summit 2027 has been issued."
        ));

        System.out.println("✅ ConfSync: Database initialization complete with full demo data!");
    }
}

