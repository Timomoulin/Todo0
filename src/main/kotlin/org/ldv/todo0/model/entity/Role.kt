package org.ldv.todo0.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

/**
 * Représente un rôle dans le système, associé à des utilisateurs.
 *
 * @property id Identifiant unique du rôle, généré automatiquement par la base de données.
 * @property nom Nom ou libellé du rôle.
 * @property utilisateurs Liste des utilisateurs associés à ce rôle.
 * La relation est de type One-to-Many avec cascade sur toutes les opérations et suppression orpheline.
 */
@Entity
class Role (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long? = null,
    var nom:String,
    @OneToMany(mappedBy = "role", cascade = [CascadeType.ALL], orphanRemoval = true)
    val utilisateurs: MutableList<Utilisateur> = mutableListOf()
)