{{ Engine requirements }}
Restitution works as shapes have bounce to them. Raycasting also detects collisions and applies the necessary impulse.

{{ Game requirements }}
Player can only jump if and only if on another object. Toggling by pressing “w” allows for raycasting to be seen, and grenade affects an area as needed. Shapes, depending on restitution, moves accordingly.
 
{{ BUGS }}
None that I know of

{{ HOURS }}
Around 20 hours.

Retries used: NONE STANDARD EXTRA ( extra retries remaining)

[ ] - Attended a design check
[ ] - Completed playtesting

Engine requirements
========================================
[ ] - Supports collision response using coefficients of restitution
[ ] - Supports raycasting against circles, polygons, and AABs

Game requirements
========================================
[ ] - Possible to witness at least one elastic, one inelastic, and one perfectly inelastic collision
[ ] - Player can fire raycast that applies an impulse on the object hit in the direction of the ray
[ ] - Player can toss a grenade that explodes on contact with any surface and pushes all objects within its radius not obstructed by other objects
[ ] - Player can jump iff standing on a solid object

Global requirements
========================================
[ ] - Handin is an Eclipse project or has an ant build script
[ ] - Has README
[ ] - README has a copy of the rubric with explanations of how to verify requirements
[ ] - README documents any known bugs
[ ] - README lists approximate hours to complete
[ ] - Has INSTRUCTIONS file containing instructions for playing the game
[ ] - No external dependencies
[ ] - Top level of all Java packages named with login
[ ] - Engine and game code in separate packages
[ ] - Engine and game code reasonably separate logically
[ ] - Engine code well-designed
[ ] - Game code reasonably designed
[ ] - Works on department machines
[ ] - Does not crash under any circumstances
[ ] - Game works within tested resolutions and aspect ratios - 960x540 (1.778:1), 900x600 (1.5:1), and 1000x750 (1.333:1)
[ ] - Game is not drawn at a permanently fixed size
[ ] - Game has sufficient performance (30 UPS generally, 20 UPS spike min)
