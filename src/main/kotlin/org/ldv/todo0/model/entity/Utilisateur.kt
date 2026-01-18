package org.ldv.todo0.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PreUpdate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime


/**
 * Entité représentant un utilisateur en base de données.
 * Elle est mappée sur une table SQL grâce à l’annotation @Entity.
 */
@Entity
class Utilisateur(

    /**
     * Identifiant unique de l'utilisateur.
     * Généré automatiquement par la base de données (auto-increment).
     * - @Id : clé primaire
     * - @GeneratedValue : stratégie de génération
     * - @Column(nullable = false) : la colonne ne peut pas être NULL
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long? = null,
    /**
     * Nom de l'utilisateur.
     */
    var nom: String,
    /**
     * Prénom de l'utilisateur.
     */
    var prenom: String,
    /**
     * Adresse e-mail de l'utilisateur.
     * Peut servir comme identifiant de connexion.
     */
    val email: String,
    /**
     * Mot de passe de l'utilisateur.
     * ⚠️ À stocker obligatoirement hashé (BCrypt) !
     */
    var mdp: String,
    /**
     * Date de création de l’enregistrement.
     * - initialisée automatiquement lors de la création
     * - non modifiable après insertion (updatable = false)
     */
    @CreationTimestamp
    @Column(updatable = false)
    val dateCreation: LocalDateTime = LocalDateTime.now(),
    /**
     * Date de dernière modification de l'enregistrement.
     * Mise à jour automatiquement avant chaque mise à jour en base (@PreUpdate).
     */
    @UpdateTimestamp
    var dateModification: LocalDateTime = LocalDateTime.now(),

    /**
     * Relation ManyToOne : un utilisateur possède un seul rôle.
     * - @JoinColumn : nom de la colonne clé étrangère (role_id)
     */
    @ManyToOne
    @JoinColumn(name = "role_id")
    val role: Role? = null
) {

}