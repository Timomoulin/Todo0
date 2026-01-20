package org.ldv.todo0.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.PreUpdate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import jakarta.validation.constraints.*


/**
 * Représente un utilisateur du système avec ses informations personnelles et ses relations.
 *
 * @property id Identifiant unique de l'utilisateur, généré automatiquement par la base de données.
 * @property nom Nom de l'utilisateur.
 * @property prenom Prénom de l'utilisateur.
 * @property email Adresse e-mail de l'utilisateur, unique dans la base. Peut être utilisée comme identifiant de connexion.
 * @property mdp Mot de passe de l'utilisateur, doit être stocké sous forme hashée (par exemple, avec BCrypt).
 * @property confirmationMdp Mot de passe de confirmation, utilisé uniquement pour des opérations de validation (non persisté en base).
 * @property dateCreation Date de création de l'enregistrement, initialisée automatiquement lors de la création et non modifiable.
 * @property dateModification Date de dernière modification de l'enregistrement, mise à jour avant chaque modification enregistrée.
 * @property role Rôle associé à cet utilisateur. La relation est de type Many-to-One.
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
    @field:NotBlank(message = "Le nom est obligatoire")
    @field:Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    var nom: String,
    /**
     * Prénom de l'utilisateur.
     */
    @field:NotBlank(message = "Le prénom est obligatoire")
    @field:Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    var prenom: String,
    /**
     * Adresse e-mail de l'utilisateur.
     * Peut servir comme identifiant de connexion.
     */
    @field:NotBlank(message = "L'email est obligatoire")
    @field:Email(message = "Format d'email invalide")
    @Column(unique = true)
    val email: String,
    /**
     * Mot de passe de l'utilisateur.
     * ⚠️ À stocker obligatoirement hashé (BCrypt) !
     */
    @field:NotBlank(message = "Le mot de passe est obligatoire")
    @field:Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    var mdp: String,
    /**
     * Mot de passe de confirmation de l'utilisateur (Transient == Propriété non-persistée ).
     *
     */
    @Transient
    var confirmationMdp: String="",
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
     * Relation ManyToZero : un utilisateur possède un seul rôle.
     * - @JoinColumn : nom de la colonne clé étrangère (role_id)
     */
    @ManyToOne
    @JoinColumn(name = "role_id")
    val role: Role? = null,
) {

}