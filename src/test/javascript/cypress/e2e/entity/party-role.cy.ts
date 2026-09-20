import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('PartyRole e2e test', () => {
  const partyRolePageUrl = '/party-role';
  let username: string;
  let password: string;
  const partyRoleSample = {};

  let partyRole;
  let party;
  let roleType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/parties',
      body: {
        isOrganisation: true,
        organisationName: 'brisk',
        organisationShortName: 'yahoo brr',
        salutation: 'handful flood instea',
        firstName: 'Rita',
        middleName: 'amused duh stylish',
        lastName: 'Hansen',
        gender: 'OTHER',
        birthDate: '2023-12-14',
        primaryPhone: 'till',
        primaryEmail: 'orXXX',
        isTemporaryPassword: true,
        logoImageUrl: 'despite questionably',
        profileImageUrl: 'lest but',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/role-types',
      body: { name: 'or pillbox per', description: 'affect perfectly yippee' },
    }).then(({ body }) => {
      roleType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/party-roles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/party-roles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/party-roles/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/role-types', {
      statusCode: 200,
      body: [roleType],
    });
  });

  afterEach(() => {
    if (partyRole) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/party-roles/${partyRole.id}`,
      }).then(() => {
        partyRole = undefined;
      });
    }
  });

  afterEach(() => {
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
    if (roleType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/role-types/${roleType.id}`,
      }).then(() => {
        roleType = undefined;
      });
    }
  });

  it('PartyRoles menu should load PartyRoles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-role');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PartyRole').should('exist');
    cy.location('pathname').should('eq', partyRolePageUrl);
  });

  describe('PartyRole page', () => {
    it('should have translated page title', () => {
      cy.visit(partyRolePageUrl);
      cy.getEntityHeading('PartyRole').should('not.contain', 'snpApp.partyRole.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(partyRolePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PartyRole page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${partyRolePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PartyRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyRolePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/party-roles',
          body: {
            ...partyRoleSample,
            party,
            roleType,
          },
        }).then(({ body }) => {
          partyRole = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/party-roles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/party-roles?page=0&size=20>; rel="last",<http://localhost/api/party-roles?page=0&size=20>; rel="first"',
              },
              body: [partyRole],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(partyRolePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PartyRole page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('partyRole');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyRolePageUrl);
      });

      it('edit button click should load edit PartyRole page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyRolePageUrl);
      });

      it('edit button click should load edit PartyRole page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyRole');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyRolePageUrl);
      });

      it('last delete button click should delete instance of PartyRole', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('partyRole').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyRolePageUrl);

        partyRole = undefined;
      });
    });
  });

  describe('new PartyRole page', () => {
    beforeEach(() => {
      cy.visit(partyRolePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PartyRole');
    });

    it('should create an instance of PartyRole', () => {
      cy.get(`[data-cy="party"]`).select(1);
      cy.get(`[data-cy="roleType"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        partyRole = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', partyRolePageUrl);
    });
  });
});
